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
import org.apache.cassandra.thrift.Mutation;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;

import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.IUserDAO;

public class UserDAO extends BaseDAO implements IUserDAO {

	@Override
	public User findByLogin(String login) {
		try {
			SlicePredicate slicePredicate = new SlicePredicate();
			SliceRange sliceRange = new SliceRange();
			sliceRange.setStart(new byte[] {});
			sliceRange.setFinish(new byte[] {});
			slicePredicate.setSlice_range(sliceRange);
			
			// get Twitter.Users['agajorte']
			ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY_USERS);
			List<ColumnOrSuperColumn> result = client.get_slice(KEYSPACE,
					login, columnParent, slicePredicate, ConsistencyLevel.ONE);

			if (result == null || result.isEmpty())
				return null;

			User user = new User();
			user.setLogin(login);

			for (ColumnOrSuperColumn c : result) {
				if (c.getColumn() != null) {
					String name = new String(c.getColumn().getName(), ENCODING);
					String value = new String(c.getColumn().getValue(), ENCODING);
					// long timestamp = c.getColumn().getTimestamp();
					if (name.equals("name")) {
						user.setName(value);
					} else if (name.equals("password")) {
						user.setPassword(value);
					}
				}
			}

			return user;
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving user by login: ", e);
		}
	}

	@Override
	public void save(User user) {
		try {
			long timestamp = System.currentTimeMillis();

            Map<String, Map<String, List<Mutation>>> job = new HashMap<String, Map<String, List<Mutation>>>();
            
			// set Twitter.Users['agajorte']['name'] = 'Rodrigo Hjort'
            Column column = new Column("name".getBytes(ENCODING), user.getName().getBytes(ENCODING), timestamp);
            ColumnOrSuperColumn columnOrSuperColumn = new ColumnOrSuperColumn();
            columnOrSuperColumn.setColumn(column);

            List<Mutation> mutations = new ArrayList<Mutation>();
            Mutation mutation = new Mutation();
            mutation.setColumn_or_supercolumn(columnOrSuperColumn);
            mutations.add(mutation);
            
			// set Twitter.Users['agajorte']['password'] = 'strongpass'
            if (user.getPassword() != null) {
	            column = new Column("password".getBytes(ENCODING), user.getPassword().getBytes(ENCODING), timestamp);
	            columnOrSuperColumn = new ColumnOrSuperColumn();
	            columnOrSuperColumn.setColumn(column);
	
	            mutation = new Mutation();
	            mutation.setColumn_or_supercolumn(columnOrSuperColumn);
	            mutations.add(mutation);
            }

            Map<String, List<Mutation>> mutationsForColumnFamily = new HashMap<String, List<Mutation>>();
            mutationsForColumnFamily.put(COLUMN_FAMILY_USERS, mutations);

            job.put(user.getLogin(), mutationsForColumnFamily);

            client.batch_mutate(KEYSPACE, job, ConsistencyLevel.ALL);
			
		} catch (Exception e) {
			throw new RuntimeException("Error saving user: ", e);
		}
	}

	@Override
	public void remove(User user) {
		try {
			
			// del Twitter.Users['agajorte']
			ColumnPath columnPath = new ColumnPath(COLUMN_FAMILY_USERS);
			client.remove(KEYSPACE, user.getLogin(), columnPath,
					System.currentTimeMillis(), ConsistencyLevel.ALL);
			
			// TODO: fazer em uma única transação
			
			// TODO: remover das CFs Friends e Followers
			// del Twitter.Friends['12313']
			// del Twitter.Followers['12313']
			// ...
			
		} catch (Exception e) {
			throw new RuntimeException("Error removing user: ", e);
		}
	}

	@Override
	public List<User> findByLogins(Iterable<String> logins) {
		
		if (logins == null)
			return null;
		
		try {
			
			List<String> keys = new ArrayList<String>();
			Iterator<String> it = logins.iterator();
			while (it.hasNext()) {
				String login = it.next();
				keys.add(login);
			}
			
			ColumnParent columnParent = new ColumnParent(COLUMN_FAMILY_USERS);
			
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

			List<User> list = null;

			for (Map.Entry<String, List<ColumnOrSuperColumn>> pair : result.entrySet()) {

				User user = new User();
				user.setLogin(pair.getKey());

				for (ColumnOrSuperColumn c : pair.getValue()) {
					if (c.getColumn() != null) {
						String name = new String(c.getColumn().getName(), ENCODING);
						String value = new String(c.getColumn().getValue(), ENCODING);
						// long timestamp = c.getColumn().getTimestamp();
						if (name.equals("name")) {
							user.setName(value);
						} else if (name.equals("password")) {
							user.setPassword(value);
						}
					}
				}
				
				if (list == null) {
					list = new ArrayList<User>();
				}
				list.add(user);
			}
			
			return list;
			
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving users by logins: ", e);
		}
	}

}
