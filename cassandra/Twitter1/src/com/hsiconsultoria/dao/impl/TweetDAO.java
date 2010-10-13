package com.hsiconsultoria.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.Mutation;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;

import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.ITweetDAO;

public class TweetDAO extends BaseDAO implements ITweetDAO {

	@Override
	public Tweet findById(Long id) {
		try {
			SlicePredicate slicePredicate = new SlicePredicate();
			SliceRange sliceRange = new SliceRange();
			sliceRange.setStart(new byte[] {});
			sliceRange.setFinish(new byte[] {});
			slicePredicate.setSlice_range(sliceRange);
			
			// get Twitter.Tweets['6769224']
			ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY_TWEETS);
			List<ColumnOrSuperColumn> result = client.get_slice(KEYSPACE,
					id.toString(), columnParent, slicePredicate,
					ConsistencyLevel.ONE);

			if (result == null || result.isEmpty())
				return null;

			Tweet tweet = new Tweet();
			tweet.setId(id);

			for (ColumnOrSuperColumn c : result) {
				if (c.getColumn() != null) {
					String name = new String(c.getColumn().getName(), ENCODING);
					String value = new String(c.getColumn().getValue(), ENCODING);
					// long timestamp = c.getColumn().getTimestamp();
					if (name.equals("user")) {
						String user = value;
						tweet.setUser(new User(user, null, null));
					} else if (name.equals("text")) {
						tweet.setText(value);
					}
				}
			}

			return tweet;
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving tweet by id: ", e);
		}
	}

	@Override
	public void save(Tweet tweet) {
		try {
			long timestamp = System.currentTimeMillis();
			User user = tweet.getUser();

            Map<String, Map<String, List<Mutation>>> job = new HashMap<String, Map<String, List<Mutation>>>();
            
			// set Twitter.Tweets['123456']['user'] = 'agajorte'
            Column column = new Column("user".getBytes(ENCODING), user.getLogin().getBytes(ENCODING), timestamp);
            ColumnOrSuperColumn columnOrSuperColumn = new ColumnOrSuperColumn();
            columnOrSuperColumn.setColumn(column);

            List<Mutation> mutations = new ArrayList<Mutation>();
            Mutation mutation = new Mutation();
            mutation.setColumn_or_supercolumn(columnOrSuperColumn);
            mutations.add(mutation);
            
			// set Twitter.Tweets['123456']['text'] = 'the message body'
            column = new Column("text".getBytes(ENCODING), tweet.getText().getBytes(ENCODING), timestamp);
            columnOrSuperColumn = new ColumnOrSuperColumn();
            columnOrSuperColumn.setColumn(column);

            mutation = new Mutation();
            mutation.setColumn_or_supercolumn(columnOrSuperColumn);
            mutations.add(mutation);

            Map<String, List<Mutation>> mutationsForColumnFamily = new HashMap<String, List<Mutation>>();
            mutationsForColumnFamily.put(COLUMN_FAMILY_TWEETS, mutations);

            job.put(tweet.getId().toString(), mutationsForColumnFamily);

            final long TIME_OFFSET = 946080000000L;
            final long ts = (long) (timestamp - TIME_OFFSET) / 1000;
//            System.out.println("to: " + TIME_OFFSET + ", timestamp: " + timestamp + ", ts: " + ts);
            
            // set Twitter.Userline['agajorte']['12345'] = '6789012345'
			column = new Column(String.valueOf(ts).getBytes(ENCODING), tweet.getId().toString().getBytes(ENCODING), timestamp);
			columnOrSuperColumn = new ColumnOrSuperColumn();
			columnOrSuperColumn.setColumn(column);
			
			mutations = new ArrayList<Mutation>();
            mutation = new Mutation();
            mutation.setColumn_or_supercolumn(columnOrSuperColumn);
            mutations.add(mutation);

            mutationsForColumnFamily = new HashMap<String, List<Mutation>>();
            mutationsForColumnFamily.put(COLUMN_FAMILY_USERLINE, mutations);

            job.put(user.getLogin(), mutationsForColumnFamily);

            List<User> followers = user.getFollowers();
            if (followers != null && !followers.isEmpty()) {
	            
				// set Twitter.Timeline['ericflo']['6789012345'] = '12345'
				// set Twitter.Timeline['yara']['6789012345'] = '12345'
				column = new Column(String.valueOf(ts).getBytes(ENCODING), tweet.getId().toString().getBytes(ENCODING), timestamp);
				columnOrSuperColumn = new ColumnOrSuperColumn();
				columnOrSuperColumn.setColumn(column);
				
				mutations = new ArrayList<Mutation>();
	            mutation = new Mutation();
	            mutation.setColumn_or_supercolumn(columnOrSuperColumn);
	            mutations.add(mutation);
	
	            mutationsForColumnFamily = new HashMap<String, List<Mutation>>();
	            mutationsForColumnFamily.put(COLUMN_FAMILY_TIMELINE, mutations);
	
	            for (User follower : followers) {
	                job.put(follower.getLogin(), mutationsForColumnFamily);
	            }
            }

            client.batch_mutate(KEYSPACE, job, ConsistencyLevel.ALL);
			
		} catch (Exception e) {
			throw new RuntimeException("Error saving tweet: ", e);
		}
	}

	@Override
	public void remove(Tweet tweet) {
        try {
	
			// del Twitter.Tweets['12345']
			ColumnPath columnPath = new ColumnPath(COLUMN_FAMILY_TWEETS);
			client.remove(KEYSPACE, tweet.getId().toString(), columnPath,
					System.currentTimeMillis(), ConsistencyLevel.ALL);
	
		} catch (Exception e) {
			throw new RuntimeException("Error removing tweet: ", e);
		}
	}

	@Override
	public List<Tweet> findByIds(Iterable<Long> ids) {

		if (ids == null)
			return null;
		
		try {
			
			List<String> keys = new ArrayList<String>();
			Iterator<Long> it = ids.iterator();
			while (it.hasNext()) {
				Long id = it.next();
				keys.add(String.valueOf(id));
			}
			
			ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY_TWEETS);
			
			KeyRange keyRange = new KeyRange();
			keyRange.setStart_key("");
			keyRange.setEnd_key("");

			SliceRange sliceRange = new SliceRange();
			sliceRange.setStart(new byte[] {});
			sliceRange.setFinish(new byte[] {});

			SlicePredicate slicePredicate = new SlicePredicate();
			slicePredicate.setSlice_range(sliceRange);

			Map<String, List<ColumnOrSuperColumn>> result = client.multiget_slice(
					KEYSPACE, keys, columnParent, slicePredicate, ConsistencyLevel.ONE);

			if (result == null || result.isEmpty())
				return null;

			List<Tweet> list = new ArrayList<Tweet>();

			for (Map.Entry<String, List<ColumnOrSuperColumn>> pair : result.entrySet()) {
				
				Tweet tweet = new Tweet();
				tweet.setId(Long.parseLong(pair.getKey()));

				for (ColumnOrSuperColumn c : pair.getValue()) {
					if (c.getColumn() != null) {
						String name = new String(c.getColumn().getName(), ENCODING);
						String value = new String(c.getColumn().getValue(), ENCODING);
						// long timestamp = c.getColumn().getTimestamp();
						if (name.equals("user")) {
							User user = new User();
							user.setLogin(value);
							tweet.setUser(user);
						} else if (name.equals("text")) {
							tweet.setText(value);
						}
					}
				}
				
				if (tweet.getUser() != null && tweet.getText() != null) {
					list.add(tweet);
				}
			}
			
			if (list.isEmpty())
				return null;
			
			return list;
			
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving tweets by ids: ", e);
		}
	}

	@Override
	public List<Long> findUserLine(User user) {
		
		int count = 50;
		
		try {

			ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY_USERLINE);

			SliceRange sliceRange = new SliceRange();
			sliceRange.setStart(new byte[] {});
			sliceRange.setFinish(new byte[] {});

			SlicePredicate slicePredicate = new SlicePredicate();
			slicePredicate.setSlice_range(sliceRange);

			List<KeySlice> result = client.get_range_slice(KEYSPACE, columnParent,
					slicePredicate, user.getLogin(), user.getLogin(), count, ConsistencyLevel.ONE);

			if (result == null || result.isEmpty())
				return null;

			List<Long> list = new ArrayList<Long>();
			
			for (KeySlice keySlice : result) {
				for (ColumnOrSuperColumn c : keySlice.columns) {
					if (c.getColumn() != null) {
						// String name = new String(c.getColumn().getName(), ENCODING);
						String value = new String(c.getColumn().getValue(), ENCODING);
						// long timestamp = c.getColumn().getTimestamp();
						String tweetId = value;
						list.add(Long.parseLong(tweetId));
					}
				}
			}

			return list;
		
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving userline by user: ", e);
		}
	}

	@Override
	public List<Long> findTimeLine(User user) {
		
		int count = 50;
		
		try {

			ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY_TIMELINE);

			SliceRange sliceRange = new SliceRange();
			sliceRange.setStart(new byte[] {});
			sliceRange.setFinish(new byte[] {});

			SlicePredicate slicePredicate = new SlicePredicate();
			slicePredicate.setSlice_range(sliceRange);

			List<KeySlice> result = client.get_range_slice(KEYSPACE, columnParent,
					slicePredicate, user.getLogin(), user.getLogin(), count, ConsistencyLevel.ONE);

			if (result == null || result.isEmpty())
				return null;

			List<Long> list = new ArrayList<Long>();
			
			for (KeySlice keySlice : result) {
				for (ColumnOrSuperColumn c : keySlice.columns) {
					if (c.getColumn() != null) {
						// String name = new String(c.getColumn().getName(), ENCODING);
						String value = new String(c.getColumn().getValue(), ENCODING);
						// long timestamp = c.getColumn().getTimestamp();
						String tweetId = value;
						list.add(Long.parseLong(tweetId));
					}
				}
			}

			return list;
		
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving timeline by user: ", e);
		}
	}

}
