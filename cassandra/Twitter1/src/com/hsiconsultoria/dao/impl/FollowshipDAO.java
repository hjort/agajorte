package com.hsiconsultoria.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.Mutation;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.IFollowshipDAO;

public class FollowshipDAO extends BaseDAO implements IFollowshipDAO {

	@Override
	public void save(Followship followship) {
		try {
			long timestamp = System.currentTimeMillis();
			
			String fromUserId = followship.getFollower().getLogin();
			String toUserId = followship.getFollowed().getLogin();
			
            Map<String, Map<String, List<Mutation>>> job = new HashMap<String, Map<String, List<Mutation>>>();
            
			// set Twitter.Following['agajorte']['ericflo'] = '1234567890'
            Column column = new Column(toUserId.getBytes(ENCODING), String.valueOf(timestamp).getBytes(ENCODING), timestamp);
            ColumnOrSuperColumn columnOrSuperColumn = new ColumnOrSuperColumn();
            columnOrSuperColumn.setColumn(column);

            List<Mutation> mutations = new ArrayList<Mutation>();
            Mutation mutation = new Mutation();
            mutation.setColumn_or_supercolumn(columnOrSuperColumn);
            mutations.add(mutation);
            
            Map<String, List<Mutation>> mutationsForColumnFamily = new HashMap<String, List<Mutation>>();
            mutationsForColumnFamily.put(COLUMN_FAMILY_FOLLOWING, mutations);

            job.put(fromUserId, mutationsForColumnFamily);

			// set Twitter.Followers['ericflo']['agajorte'] = '1234567890'
			column = new Column(fromUserId.getBytes(ENCODING), String.valueOf(timestamp).getBytes(ENCODING), timestamp);
			columnOrSuperColumn = new ColumnOrSuperColumn();
			columnOrSuperColumn.setColumn(column);
			
			mutations = new ArrayList<Mutation>();
            mutation = new Mutation();
            mutation.setColumn_or_supercolumn(columnOrSuperColumn);
            mutations.add(mutation);

            mutationsForColumnFamily = new HashMap<String, List<Mutation>>();
            mutationsForColumnFamily.put(COLUMN_FAMILY_FOLLOWERS, mutations);

            job.put(toUserId, mutationsForColumnFamily);
            
            client.batch_mutate(KEYSPACE, job, ConsistencyLevel.ALL);
			
		} catch (Exception e) {
			throw new RuntimeException("Error saving followship: ", e);
		}
	}

	@Override
	public void remove(Followship followship) {
        try {
			String fromUserId = followship.getFollower().getLogin();
			String toUserId = followship.getFollowed().getLogin();
	
			// TODO: fazer em uma única transação
	//      RowMutation rm = new RowMutation(table, key);
	//      rm.delete(new QueryPath(column_path), timestamp);
	
			// del Twitter.Following['1231']
			ColumnPath columnPath = new ColumnPath(COLUMN_FAMILY_FOLLOWING);
			client.remove(KEYSPACE, fromUserId, columnPath,
					System.currentTimeMillis(), ConsistencyLevel.ALL);
	
			// del Twitter.Followers['3874']
			columnPath = new ColumnPath(COLUMN_FAMILY_FOLLOWERS);
			client.remove(KEYSPACE, toUserId, columnPath,
					System.currentTimeMillis(), ConsistencyLevel.ALL);
			
		} catch (Exception e) {
			throw new RuntimeException("Error removing followship: ", e);
		}
	}

	@Override
	public List<String> findFollowingsLogins(User followingUser) {
        try {
        	String key = followingUser.getLogin();
		
	        SlicePredicate slicePredicate = new SlicePredicate();
	        SliceRange sliceRange = new SliceRange();
	        sliceRange.setStart(new byte[] {});
	        sliceRange.setFinish(new byte[] {});
	        slicePredicate.setSlice_range(sliceRange);
	
	        ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY_FOLLOWING);
	        List<ColumnOrSuperColumn> result = client.get_slice(KEYSPACE,
	        		key, columnParent, slicePredicate, ConsistencyLevel.ONE);
	        
			if (result == null || result.isEmpty())
				return null;

			List<String> list = new ArrayList<String>();
			for (ColumnOrSuperColumn c : result) {
				if (c.getColumn() != null) {
					String name = new String(c.getColumn().getName(), ENCODING);
					// String value = new String(c.getColumn().getValue(), ENCODING);
					// long timestamp = c.getColumn().getTimestamp();
					String login = name;
					list.add(login);
				}
			}

			return list;
			
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving followings logins: ", e);
        }
	}

	@Override
	public List<String> findFollowersLogins(User followed) {
        try {
        	String key = followed.getLogin();
		
	        SlicePredicate slicePredicate = new SlicePredicate();
	        SliceRange sliceRange = new SliceRange();
	        sliceRange.setStart(new byte[] {});
	        sliceRange.setFinish(new byte[] {});
	        slicePredicate.setSlice_range(sliceRange);
	
	        ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY_FOLLOWERS);
	        List<ColumnOrSuperColumn> result = client.get_slice(KEYSPACE,
	        		key, columnParent, slicePredicate, ConsistencyLevel.ONE);
	        
			if (result == null || result.isEmpty())
				return null;

			List<String> list = new ArrayList<String>();
			for (ColumnOrSuperColumn c : result) {
				if (c.getColumn() != null) {
					String name = new String(c.getColumn().getName(), ENCODING);
					// String value = new String(c.getColumn().getValue(), ENCODING);
					// long timestamp = c.getColumn().getTimestamp();
					String login = name;
					list.add(login);
				}
			}

			return list;
			
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving followers logins: ", e);
        }
	}

}
