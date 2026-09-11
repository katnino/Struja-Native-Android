package com.noniboy.struja.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.noniboy.struja.data.db.dao.BillDao;
import com.noniboy.struja.data.db.dao.BillDao_Impl;
import com.noniboy.struja.data.db.dao.MeterDao;
import com.noniboy.struja.data.db.dao.MeterDao_Impl;
import com.noniboy.struja.data.db.dao.ReadingDao;
import com.noniboy.struja.data.db.dao.ReadingDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StrujaDatabase_Impl extends StrujaDatabase {
  private volatile MeterDao _meterDao;

  private volatile ReadingDao _readingDao;

  private volatile BillDao _billDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `meters` (`id` TEXT NOT NULL, `userId` TEXT NOT NULL, `name` TEXT NOT NULL, `tariffGroup` TEXT NOT NULL, `approvedKw` REAL NOT NULL, `notes` TEXT, `createdAt` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `readings` (`id` TEXT NOT NULL, `meterId` TEXT NOT NULL, `userId` TEXT NOT NULL, `recordedAt` TEXT NOT NULL, `vt` INTEGER, `mt` INTEGER, `source` TEXT NOT NULL, `confidence` TEXT, `createdAt` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`meterId`) REFERENCES `meters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_readings_meterId` ON `readings` (`meterId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bills` (`id` TEXT NOT NULL, `meterId` TEXT NOT NULL, `userId` TEXT NOT NULL, `periodStart` TEXT NOT NULL, `periodEnd` TEXT NOT NULL, `prevReadingId` TEXT, `currReadingId` TEXT, `approvedKw` REAL NOT NULL, `consumptionKwh` REAL NOT NULL, `mjernoMjesto` REAL NOT NULL, `obracunskaSnaga` REAL NOT NULL, `energyCost` REAL NOT NULL, `transmissionBaseCost` REAL NOT NULL, `totalTransmission` REAL NOT NULL, `distributionBaseCost` REAL NOT NULL, `totalDistribution` REAL NOT NULL, `oieCost` REAL NOT NULL, `subtotal` REAL NOT NULL, `vatAmount` REAL NOT NULL, `total` REAL NOT NULL, `blocksJson` TEXT NOT NULL, `isPartial` INTEGER NOT NULL, `createdAt` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`meterId`) REFERENCES `meters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_bills_meterId` ON `bills` (`meterId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '078dad88afee96b4c5df0d5ea0c5a63a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `meters`");
        db.execSQL("DROP TABLE IF EXISTS `readings`");
        db.execSQL("DROP TABLE IF EXISTS `bills`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMeters = new HashMap<String, TableInfo.Column>(7);
        _columnsMeters.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("tariffGroup", new TableInfo.Column("tariffGroup", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("approvedKw", new TableInfo.Column("approvedKw", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeters.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMeters = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMeters = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMeters = new TableInfo("meters", _columnsMeters, _foreignKeysMeters, _indicesMeters);
        final TableInfo _existingMeters = TableInfo.read(db, "meters");
        if (!_infoMeters.equals(_existingMeters)) {
          return new RoomOpenHelper.ValidationResult(false, "meters(com.noniboy.struja.data.db.entity.MeterEntity).\n"
                  + " Expected:\n" + _infoMeters + "\n"
                  + " Found:\n" + _existingMeters);
        }
        final HashMap<String, TableInfo.Column> _columnsReadings = new HashMap<String, TableInfo.Column>(9);
        _columnsReadings.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadings.put("meterId", new TableInfo.Column("meterId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadings.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadings.put("recordedAt", new TableInfo.Column("recordedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadings.put("vt", new TableInfo.Column("vt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadings.put("mt", new TableInfo.Column("mt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadings.put("source", new TableInfo.Column("source", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadings.put("confidence", new TableInfo.Column("confidence", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadings.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReadings = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysReadings.add(new TableInfo.ForeignKey("meters", "CASCADE", "NO ACTION", Arrays.asList("meterId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesReadings = new HashSet<TableInfo.Index>(1);
        _indicesReadings.add(new TableInfo.Index("index_readings_meterId", false, Arrays.asList("meterId"), Arrays.asList("ASC")));
        final TableInfo _infoReadings = new TableInfo("readings", _columnsReadings, _foreignKeysReadings, _indicesReadings);
        final TableInfo _existingReadings = TableInfo.read(db, "readings");
        if (!_infoReadings.equals(_existingReadings)) {
          return new RoomOpenHelper.ValidationResult(false, "readings(com.noniboy.struja.data.db.entity.ReadingEntity).\n"
                  + " Expected:\n" + _infoReadings + "\n"
                  + " Found:\n" + _existingReadings);
        }
        final HashMap<String, TableInfo.Column> _columnsBills = new HashMap<String, TableInfo.Column>(23);
        _columnsBills.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("meterId", new TableInfo.Column("meterId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("periodStart", new TableInfo.Column("periodStart", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("periodEnd", new TableInfo.Column("periodEnd", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("prevReadingId", new TableInfo.Column("prevReadingId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("currReadingId", new TableInfo.Column("currReadingId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("approvedKw", new TableInfo.Column("approvedKw", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("consumptionKwh", new TableInfo.Column("consumptionKwh", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("mjernoMjesto", new TableInfo.Column("mjernoMjesto", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("obracunskaSnaga", new TableInfo.Column("obracunskaSnaga", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("energyCost", new TableInfo.Column("energyCost", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("transmissionBaseCost", new TableInfo.Column("transmissionBaseCost", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("totalTransmission", new TableInfo.Column("totalTransmission", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("distributionBaseCost", new TableInfo.Column("distributionBaseCost", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("totalDistribution", new TableInfo.Column("totalDistribution", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("oieCost", new TableInfo.Column("oieCost", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("subtotal", new TableInfo.Column("subtotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("vatAmount", new TableInfo.Column("vatAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("total", new TableInfo.Column("total", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("blocksJson", new TableInfo.Column("blocksJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("isPartial", new TableInfo.Column("isPartial", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBills = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBills.add(new TableInfo.ForeignKey("meters", "CASCADE", "NO ACTION", Arrays.asList("meterId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBills = new HashSet<TableInfo.Index>(1);
        _indicesBills.add(new TableInfo.Index("index_bills_meterId", false, Arrays.asList("meterId"), Arrays.asList("ASC")));
        final TableInfo _infoBills = new TableInfo("bills", _columnsBills, _foreignKeysBills, _indicesBills);
        final TableInfo _existingBills = TableInfo.read(db, "bills");
        if (!_infoBills.equals(_existingBills)) {
          return new RoomOpenHelper.ValidationResult(false, "bills(com.noniboy.struja.data.db.entity.BillEntity).\n"
                  + " Expected:\n" + _infoBills + "\n"
                  + " Found:\n" + _existingBills);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "078dad88afee96b4c5df0d5ea0c5a63a", "7a687cffa5da89e6cfe3f72220d1b071");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "meters","readings","bills");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `meters`");
      _db.execSQL("DELETE FROM `readings`");
      _db.execSQL("DELETE FROM `bills`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MeterDao.class, MeterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReadingDao.class, ReadingDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BillDao.class, BillDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public MeterDao meterDao() {
    if (_meterDao != null) {
      return _meterDao;
    } else {
      synchronized(this) {
        if(_meterDao == null) {
          _meterDao = new MeterDao_Impl(this);
        }
        return _meterDao;
      }
    }
  }

  @Override
  public ReadingDao readingDao() {
    if (_readingDao != null) {
      return _readingDao;
    } else {
      synchronized(this) {
        if(_readingDao == null) {
          _readingDao = new ReadingDao_Impl(this);
        }
        return _readingDao;
      }
    }
  }

  @Override
  public BillDao billDao() {
    if (_billDao != null) {
      return _billDao;
    } else {
      synchronized(this) {
        if(_billDao == null) {
          _billDao = new BillDao_Impl(this);
        }
        return _billDao;
      }
    }
  }
}
