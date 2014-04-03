package org.dutir.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import org.dutir.util.NumConversions;
import org.dutir.util.ObjectSerializer;
import org.dutir.util.Pair;

import com.sleepycat.db.BtreeStats;
import com.sleepycat.db.Cursor;
import com.sleepycat.db.CursorConfig;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.db.LockMode;
import com.sleepycat.db.OperationStatus;

/**
 * @author yezheng
 */

public class BerkeleyBT {

	/**
	 * test
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			BerkeleyBT bdb = new BerkeleyBT("./testbdb", DBConstant.READ_WRITE,
					10);
			////test iterator/////
			bdb.putObject("12", "1234");
			bdb.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * setup current db using dbtype para.
	 * 
	 * @param dbtype
	 *            0:en , 1:cn , 2 : int
	 */
	public void setCurrentDB(int dbtype) {
		intiDB();
		if (dbtype == 0) {
			this.currentDb = this.enStringBytesDb;
		} else if (dbtype == 1) {
			this.currentDb = this.cnStringBytesDb;
		} else if (dbtype == 2) {
			this.currentDb = this.intBytesDb;
		}
	}

	public void intiDB() {
		try {
			if (this.cnStringBytesDb == null) {
				this.cnStringBytesDb = this.env.openDatabase(null,
						this.fileName + "cnStringBytes.dbx", null,
						this.databaseConfig);

				if (this.mode == DBConstant.WRITE_ONLY) {
					System.err
							.println("Clearing out an existing database, removed "
									+ this.cnStringBytesDb.truncate(null, true)
									+ " records");

				}
			}
			if (this.enStringBytesDb == null) {
				this.enStringBytesDb = this.env.openDatabase(null,
						this.fileName + "enStringBytes.dbx", null,
						this.databaseConfig);
				if (this.mode == DBConstant.WRITE_ONLY) {
					System.err
							.println("Clearing out an existing database, removed "
									+ this.enStringBytesDb.truncate(null, true)
									+ " records");

				}
			}
			if (this.intBytesDb == null) {
				this.intBytesDb = this.env.openDatabase(null, this.fileName
						+ "IntBytes.dbx", null, this.databaseConfig);
				if (this.mode == DBConstant.WRITE_ONLY) {
					System.err
							.println("Clearing out an existing database, removed "
									+ this.intBytesDb.truncate(null, true)
									+ " records");

				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Iterator<Pair> iterator() {
		Iterator<Pair> returnValue = null;
		CursorConfig config = new CursorConfig();
		config.setDirtyRead(true);
		Cursor cursor;
		try {
			cursor = this.currentDb.openCursor(null, null);
			ArrayList keys = new ArrayList();

			// ---------------------------------------
			// Determine if the keys are int or String
			// ---------------------------------------
			boolean stringKeys = stringKeys = isStringKey(this.currentDb);

			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry data = new DatabaseEntry();

			OperationStatus status = cursor.getNext(key, data, null);

			Object o = null;
			int count = 0;
			while (status == OperationStatus.SUCCESS) {

				if (stringKeys) {
					o = ObjectSerializer.unSerializeObject(data.getData(),
							compressTag);
					keys.add(new Pair<Object, Object>(
							new String(key.getData()), o));
				}

				else {
					o = ObjectSerializer.unSerializeObject(data.getData(),
							compressTag);
					keys.add(new Pair<Object, Object>(new Integer(
							NumConversions.bytesToInt(key.getData())), o));
				}
				key = new DatabaseEntry();
				data = new DatabaseEntry();
				status = cursor.getNext(key, data, null);
				//				System.out.println("count: " + count);
				if (count++ % 1000 == 0) {
					System.out.println("count: " + count);
				}
			}
			returnValue = keys.iterator();
			cursor.close();
			cursor = null;

		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return (returnValue);
	}

	public Iterator keyIterator() throws Exception {
		Iterator returnValue = null;
		//		CursorConfig config = new CursorConfig();
		//		config.setDirtyRead(true);
		Cursor cursor = this.currentDb.openCursor(null, null);
		ArrayList keys = new ArrayList();

		// ---------------------------------------
		// Determine if the keys are int or String
		// ---------------------------------------
		boolean stringKeys = isStringKey(this.currentDb);
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();

		OperationStatus status = cursor.getNextNoDup(key, data, null);

		while (status == OperationStatus.SUCCESS) {
			if (stringKeys)
				keys.add(new String(key.getData()));
			else
				keys.add(new Integer(NumConversions.bytesToInt(key.getData())));
			key = new DatabaseEntry();
			data = new DatabaseEntry();
			status = cursor.getNextNoDup(key, data, null);
		}
		returnValue = keys.iterator();
		cursor.close();
		cursor = null;
		return (returnValue);
	}

	/**
	 * This is a constructor for BerkeleyBtreeJ
	 * 
	 * 
	 * @param pFileName
	 * @param pMode
	 *            JDBM.WRITE_ONLY|JDBM.READ_WRITE|JDBM.READ_ONLY
	 * @param pLoadCacheSize
	 *            (in Megabytes) the size to suck into memory initially
	 * @param pMaxCacheSize
	 *            (in Megabytes) the max size of the cache
	 * @exception Exception
	 */
	// ================================================|Constructor |====
	public BerkeleyBT(String pFileName, int pMode, int pMaxCacheSize)
			throws Exception {
		this.mode = pMode;

		// -------------------------------------------------------
		// figure out the name of the directory from the full path
		// -------------------------------------------------------
		File aFile = new File(pFileName);

		this.directory = aFile.getAbsolutePath();
		this.fileName = aFile.getName();
		File directoryHandle = new File(pFileName);

		if (pMode == DBConstant.READ_WRITE) {
			if (!directoryHandle.exists() || !directoryHandle.isDirectory()) {
				this.mode = DBConstant.WRITE_ONLY;
			} else {
				String files[] = directoryHandle.list();
				if (files == null || files.length < 1) {
					this.mode = DBConstant.WRITE_ONLY;
				}
			}

		}
		this.environmentConfig = new EnvironmentConfig();
		this.environmentConfig.setTransactional(false);

		if (pMaxCacheSize >= 1) {
			this.environmentConfig.setCacheSize(pMaxCacheSize
					* BYTES_IN_MEGABYTE);
		}

		// -------------------------------------------
		// Create a database Config Object
		// -------------------------------------------
		this.databaseConfig = new DatabaseConfig();
		this.databaseConfig.setTransactional(false);

		// -------------------------------------------
		// Switch on Database Mode
		// -------------------------------------------

		switch (this.mode) {

		case DBConstant.WRITE_ONLY:

			this.environmentConfig.setAllowCreate(true);
			// this.environmentConfig.setReadOnly(false);
			this.environmentConfig.setTransactional(false);

			this.databaseConfig.setAllowCreate(true);
			this.databaseConfig.setReadOnly(false);
			this.databaseConfig.setTransactional(false);

			// -------------------------------------------------------
			// If this directory does not exist, and the mode
			// is not read-only, make the directory
			// -------------------------------------------------------
			File dir = new File(this.directory);
			if ((!dir.exists()) || (!dir.isDirectory())) {
				dir.mkdirs();
			} else {

			}

			break;

		case DBConstant.READ_WRITE:
			this.environmentConfig.setAllowCreate(true);
			this.environmentConfig.setTransactional(false);
			this.databaseConfig.setAllowCreate(false);
			this.databaseConfig.setReadOnly(false);
			this.databaseConfig.setTransactional(false);

			break;
		case DBConstant.READ_ONLY:

			this.environmentConfig.setAllowCreate(false);
			this.databaseConfig.setAllowCreate(false);
			this.databaseConfig.setReadOnly(true);

			break;

		}

		// ------------------------------------
		// Create a database Environment Object
		// ------------------------------------
		this.databaseConfig.setType(DatabaseType.BTREE);
		this.databaseConfig.setSortedDuplicates(true);
		this.environmentConfig.setInitializeCache(true);
		this.env = new Environment(directoryHandle, this.environmentConfig);
		//		this.env.removeOldLogFiles();
		System.out.println("BerkeleyBT:" + "initial currentdb");

	} // ***End Constructor

	public void close() throws Exception {

		if (this.cnStringBytesDb != null)
			this.cnStringBytesDb.close();

		if (this.enStringBytesDb != null)
			this.enStringBytesDb.close();

		if (this.intIntDb != null)
			this.intIntDb.close();

		if (this.intBytesDb != null)
			this.intBytesDb.close();

		if (this.env != null) {
			this.env.close();
		}
		this.cnStringBytesDb = null;
		this.enStringBytesDb = null;

		this.intIntDb = null;
		this.intBytesDb = null;

		this.env = null;

	}

	public void putObject(String pKey, Object pValue) throws Exception {

		// --------------------------------------
		// Serialize the object before storing it
		// --------------------------------------
		byte[] bytes = ObjectSerializer.serializeObject(pValue, compressTag);
		this.putBytes(pKey, bytes); // puts into the stringObjectDb
	}

	public void putBytes(String pKey, byte[] pValue) throws Exception {

		if (this.mode == DBConstant.READ_ONLY) {
			throw new Exception("The mode is read only, cannot add to the db");
		}
		setDB(pKey);

		DatabaseEntry dKey = new DatabaseEntry();
		DatabaseEntry dValue = new DatabaseEntry();
		dKey.setData(pKey.getBytes());
		dValue.setData(pValue);

		OperationStatus status = this.currentDb.put(null, dKey, dValue);
		if (!status.equals(OperationStatus.SUCCESS)) {
			System.err
					.println("Something went wrong inserting into the stringBytes table");
		}

	} // public void putBytes(String pKey, byte[] pValue ) throws Exception {


	public void putBytes(int pKey, byte[] pValue) throws Exception {

		if (this.mode == DBConstant.READ_ONLY) {
			throw new Exception("The mode is read only, cannot add to the db");
		}

		if (this.intBytesDb == null) {
			this.intBytesDb = this.env.openDatabase(null, this.fileName
					+ "IntBytes.dbx", null, this.databaseConfig);

			if (this.mode == DBConstant.WRITE_ONLY) {
				System.err
						.println("Clearing out an existing database, removed "
								+ this.intBytesDb.truncate(null, true)
								+ " records");

			}
		}

		this.currentDb = this.intBytesDb;

		DatabaseEntry dKey = new DatabaseEntry();
		DatabaseEntry dValue = new DatabaseEntry();

		dKey.setData(NumConversions.intToBytes(pKey));
		dValue.setData(pValue);

		this.intBytesDb.put(null, dKey, dValue);

	} // public void putBytes(int pKey, byte[] pValue )

	// ================================================|Public Method
	// Header|====
	/**
	 * Method put
	 * 
	 * @param pKey
	 * @param pValue
	 * @exception Exception
	 * 
	 */
	// ================================================|Public Method
	// Header|====
	public void put(int pKey, int pValue) throws Exception {

		if (this.mode == DBConstant.READ_ONLY) {
			throw new Exception("The mode is read only, cannot add to the db");
		}

		if (this.intIntDb == null) {

			this.intIntDb = this.env.openDatabase(null, this.fileName
					+ "IntInt.dbx", null, this.databaseConfig);

			// --------------------------------
			// Make sure this database is empty
			// before writing to it.
			// --------------------------------
			if (this.mode == DBConstant.WRITE_ONLY) {

				System.err
						.println("Clearing out an existing database, removed "
								+ this.intIntDb.truncate(null, true)
								+ " records");

			}
		}
		this.currentDb = this.intIntDb;

		DatabaseEntry dKey = new DatabaseEntry();
		DatabaseEntry dValue = new DatabaseEntry();

		dKey.setData(NumConversions.intToBytes(pKey));
		dValue.setData(NumConversions.intToBytes(pValue));
		this.intIntDb.put(null, dKey, dValue);
	}

	// ================================================|Public Method
	// Header|====
	/**
	 * Method getObject
	 * 
	 * @param pRowId
	 * @return Object
	 * 
	 * @exception Exception
	 */
	// ================================================|Public Method
	// Header|====
	public Object getObject(String pRowId) throws Exception {

		Object returnValue = null;
		byte[] bytes = null;

		bytes = this.getBytes(pRowId); // retrieves from the StringObjectDb

		if (bytes != null)
			returnValue = ObjectSerializer
					.unSerializeObject(bytes, compressTag);

		return (returnValue);

	} // public Object getObject(String pRowId)

	public boolean contain(String key){
		setDB(key);
		DatabaseEntry dKey = new DatabaseEntry();
		DatabaseEntry dValue = new DatabaseEntry();
		dKey.setData(key.getBytes());
		OperationStatus status =null;
		try {
			status = this.currentDb.get(null, dKey, dValue, null);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		if (status != null && status.equals(OperationStatus.SUCCESS)) {
			return true;
		} 
		return false;
	}
	
	/**
	 * set the current DB, this is not thread-safe
	 * @param key
	 */
	public void setDB(String key) {
		boolean en_cn = true;
		String tag;
		try {
			if (Character.getType(key.charAt(0)) != 5) {
				this.currentDb = this.enStringBytesDb;
				tag = "enStringBytes.dbx";
			} else {
				this.currentDb = this.cnStringBytesDb;
				tag = "cnStringBytes.dbx";
				en_cn = false;
			}
			if (this.currentDb == null) {
				/**
				 * �˴��޸�Ϊ���Բ����ظ���Key
				 */
				this.currentDb = this.env.openDatabase(null, this.fileName
						+ tag, null, this.databaseConfig);

				if (this.mode == DBConstant.WRITE_ONLY) {
					System.err
							.println("Clearing out an existing database, removed "
									+ this.currentDb.truncate(null, true)
									+ " records");
				}
				if (en_cn) {
					this.enStringBytesDb = this.currentDb;
				} else {
					this.cnStringBytesDb = this.currentDb;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Object[] getObjects(String key) throws Exception {
		try {
			if (key == null || key.length() < 1)
				return null;
			setDB(key);
			Cursor cursor = this.currentDb.openCursor(null, null);
			DatabaseEntry dKey = new DatabaseEntry(key.getBytes());
			DatabaseEntry dValue = new DatabaseEntry();
			ArrayList list = new ArrayList();
			OperationStatus status = cursor.getSearchKey(dKey, dValue,
					LockMode.DEFAULT);

			Object o = null;
			while (status == OperationStatus.SUCCESS) {
				o = ObjectSerializer.unSerializeObject(dValue.getData(),
						compressTag);
				list.add(o);
				dValue = new DatabaseEntry();
				status = cursor.getNextDup(dKey, dValue, LockMode.DEFAULT);
			}
			cursor.close();
			return list.toArray();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	// ================================================|Public Method
	// Header|====
	/**
	 * Method getBytes
	 * 
	 * @param pKey
	 * @return byte[]
	 * 
	 * @exception Exception
	 * 
	 */
	// ================================================|Public Method
	// Header|====
	public byte[] getBytes(String pKey) throws Exception {

		byte returnValue[] = null;
		setDB(pKey);
		DatabaseEntry dKey = new DatabaseEntry();
		DatabaseEntry dValue = new DatabaseEntry();
		dKey.setData(pKey.getBytes());

		OperationStatus status = this.currentDb.get(null, dKey, dValue, null);

		if (status.equals(OperationStatus.SUCCESS)) {
			returnValue = dValue.getData();
		} else if (status.equals(OperationStatus.KEYEXIST))
			throw new Exception(
					"The operation to insert data was configured not to allow overwrite and the key already exists in the database");
		return (returnValue);

	} // public byte[] getBytes(String pRowId)

	public byte[] getBytes(int pKey) throws Exception {

		byte returnValue[] = null;

		if (this.intBytesDb == null) {
			this.intBytesDb = this.env.openDatabase(null, this.fileName
					+ "IntBytes.dbx", null, this.databaseConfig);

		}
		this.currentDb = this.intBytesDb;
		DatabaseEntry dKey = new DatabaseEntry();
		DatabaseEntry dValue = new DatabaseEntry();

		dKey.setData(NumConversions.intToBytes(pKey));

		OperationStatus status = this.intBytesDb.get(null, dKey, dValue, null);
		if (status.equals(OperationStatus.SUCCESS))
			returnValue = dValue.getData();
		else if (status.equals(OperationStatus.NOTFOUND))
			// Do nothing - it's not found
			;
		else if (status.equals(OperationStatus.KEYEMPTY))
			throw new Exception(
					"The cursor operation was unsuccessful because the current record was deleted");
		else if (status.equals(OperationStatus.KEYEXIST))
			throw new Exception(
					"The operation to insert data was configured not to allow overwrite and the key already exists in the database");

		return (returnValue);

	}

	// ================================================|Public Method
	// Header|====
	/**
	 * Method getObject
	 * 
	 * @param pRowId
	 * @return Object
	 * 
	 * @exception Exception
	 * 
	 */
	// ================================================|Public Method
	// Header|====
	public Object getObject(int pRowId) throws Exception {

		Object returnValue = null;

		byte[] bytes = this.getBytes(pRowId); // retrieves from intObjectDb

		if (bytes != null)
			returnValue = ObjectSerializer
					.unSerializeObject(bytes, compressTag);

		return (returnValue);

	}

	// ================================================|Public Method
	// Header|====
	/**
	 * Method getInts retrieves an array of ints from the datastore using a
	 * string as the key
	 * 
	 * @param pKey
	 * @return int[]
	 * 
	 * @exception Exception
	 * 
	 */
	// ================================================|Public Method
	// Header|====
	public int[] getInts(String pKey) throws Exception {

		int returnValue[] = null;

		byte[] bytes = null;

		bytes = this.getBytes(pKey); // retrieves from StringObjectDb

		if (bytes != null) {
			// -----------------------------
			// Convert the bytes into int[]
			// -----------------------------
			returnValue = NumConversions.bytesToInts(bytes);
		}

		return (returnValue);
	}

	// ================================================|Public Method
	// Header|====
	/**
	 * Method getInts retrieves an array of ints from the datastore using an int
	 * as the key
	 * 
	 * @param pKey
	 * @return int[]
	 * 
	 * @exception Exception
	 * 
	 */
	// ================================================|Public Method
	// Header|====
	public int[] getInts(int pKey) throws Exception {
		int returnValue[] = null;
		try {
			byte[] bytes = this.getBytes(pKey); // retrieves from intObjectDb

			if (bytes != null)
				returnValue = NumConversions.bytesToInts(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (returnValue);

	}

	public int get(long pRowId) throws Exception {
		int returnValue = -1;
		if (this.intIntDb == null) {
			this.intIntDb = this.env.openDatabase(null, this.fileName
					+ "IntInt.dbx", null, this.databaseConfig);
		}
		this.currentDb = this.intIntDb;

		DatabaseEntry dKey = new DatabaseEntry();
		DatabaseEntry dValue = new DatabaseEntry();

		dKey.setData(NumConversions.intToBytes((int) pRowId));

		OperationStatus status = this.intIntDb.get(null, dKey, dValue, null);

		if (status.equals(OperationStatus.SUCCESS))
			returnValue = NumConversions.bytesToInt(dValue.getData());

		else if (status.equals(OperationStatus.NOTFOUND))
			throw new Exception(
					"The cursor operation was unsuccessful because the current record was deleted");
		else if (status.equals(OperationStatus.KEYEXIST))
			throw new Exception(
					"The operation to insert data was configured not to allow overwrite and the key already exists in the database");

		return (returnValue);

	} // public int get(long pRowId)

	// ================================================|Public Method
	// Header|====
	/**
	 * Method isStringKey returns true if the the table key is of the type
	 * String
	 * 
	 * @param pCurrentDatabase
	 * @return boolean
	 * 
	 */
	// ================================================|Public Method
	// Header|====
	public boolean isStringKey(Database pCurrentDatabase) {

		boolean returnValue = false;

		if ((pCurrentDatabase == cnStringBytesDb)
				|| (pCurrentDatabase == enStringBytesDb)) {
			returnValue = true;
		}

		return (returnValue);
	}

	public long getNumberOfRows(Database database) {
		long returnValue = 0;
		try {
			BtreeStats someStats = (BtreeStats) database.getStats(null, null);
			returnValue = someStats.getNumData();
		} catch (Exception e) {
			returnValue = 0;
		}
		return (returnValue);
	}

	/**
	 * return the max num of the
	 * 
	 * @return
	 */
	public int getMaxNumOfIntBytesDB() {
		if (this.intBytesDb == null) {

			if (this.mode == DBConstant.WRITE_ONLY) {
				return -1;
			} else if (this.mode == DBConstant.READ_WRITE) {
				try {
					this.intBytesDb = this.env.openDatabase(null, this.fileName
							+ "IntBytes.dbx", null, this.databaseConfig);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}

		}
		Cursor cursor = null;
		try {
			cursor = this.intBytesDb.openCursor(null, null);
			DatabaseEntry dKey = new DatabaseEntry();
			DatabaseEntry dValue = new DatabaseEntry();
			cursor.getPrev(dKey, dValue, LockMode.DEFAULT);
			cursor.close();
			return NumConversions.bytesToInt(dKey.getData());
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return -1;
		// return getNumberOfRows(this.intBytesDb);
	}

	public void setMaxCacheSize(int size) {
		try {
			this.env.getConfig().setCacheSize(size * BYTES_IN_MEGABYTE);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	public long getNumberOfEnKeyRows() {
		if (this.enStringBytesDb == null) {
			try {
				enStringBytesDb = this.env.openDatabase(null, this.fileName
						+ "enStringBytes.dbx", null, this.databaseConfig);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
		return getNumberOfRows(this.enStringBytesDb);
	}

	public long getNumberOfCnKeyRows() {
		if (this.cnStringBytesDb == null) {
			try {
				cnStringBytesDb = this.env.openDatabase(null, this.fileName
						+ "cnStringBytes.dbx", null, this.databaseConfig);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
		return getNumberOfRows(this.cnStringBytesDb);
	}

	//	
	public long getNumberOfConceptKeyRows() {
		if (this.intBytesDb == null) {
			try {
				intBytesDb = this.env.openDatabase(null, this.fileName
						+ "IntBytes.dbx", null, this.databaseConfig);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
		return getNumberOfRows(this.intBytesDb);
	}

	// ====================
	// Private Declarations
	// ====================
	private int mode = DBConstant.READ_ONLY;

	public Database enStringBytesDb = null;

	private Database cnStringBytesDb = null;

	private Database intBytesDb = null;

	private Database intIntDb = null;

	private Database currentDb = null;

	private String directory = null;

	private String fileName = null;

	private DatabaseConfig databaseConfig = null;

	private Environment env = null;

	private EnvironmentConfig environmentConfig = null;

	private static final int BYTES_IN_MEGABYTE = (1024 * 1024); // 1,048,576
	private static boolean compressTag = true;// the tag to detemine whether

	// an object will be compressed
	// before store

	public Database getCnStringBytesDb() {
		if (this.cnStringBytesDb == null) {

			try {
				this.cnStringBytesDb = this.env.openDatabase(null,
						this.fileName + "cnStringBytes.dbx", null,
						this.databaseConfig);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}

		}
		return this.cnStringBytesDb;
	}

	public Database getEnStringBytesDb() {
		if (this.enStringBytesDb == null) {

			try {
				this.enStringBytesDb = this.env.openDatabase(null,
						this.fileName + "enStringBytes.dbx", null,
						this.databaseConfig);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
		return enStringBytesDb;
	}

} 

