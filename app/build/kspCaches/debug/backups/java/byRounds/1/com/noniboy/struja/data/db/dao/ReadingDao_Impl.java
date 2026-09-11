package com.noniboy.struja.data.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.noniboy.struja.data.db.entity.ReadingEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReadingDao_Impl implements ReadingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReadingEntity> __insertionAdapterOfReadingEntity;

  public ReadingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReadingEntity = new EntityInsertionAdapter<ReadingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `readings` (`id`,`meterId`,`userId`,`recordedAt`,`vt`,`mt`,`source`,`confidence`,`createdAt`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReadingEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getMeterId());
        statement.bindString(3, entity.getUserId());
        statement.bindString(4, entity.getRecordedAt());
        if (entity.getVt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getVt());
        }
        if (entity.getMt() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getMt());
        }
        statement.bindString(7, entity.getSource());
        if (entity.getConfidence() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getConfidence());
        }
        statement.bindString(9, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object insert(final ReadingEntity reading, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfReadingEntity.insert(reading);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReadingEntity>> getByMeterId(final String meterId) {
    final String _sql = "SELECT * FROM readings WHERE meterId = ? ORDER BY recordedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, meterId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"readings"}, new Callable<List<ReadingEntity>>() {
      @Override
      @NonNull
      public List<ReadingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "meterId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfRecordedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedAt");
          final int _cursorIndexOfVt = CursorUtil.getColumnIndexOrThrow(_cursor, "vt");
          final int _cursorIndexOfMt = CursorUtil.getColumnIndexOrThrow(_cursor, "mt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ReadingEntity> _result = new ArrayList<ReadingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReadingEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMeterId;
            _tmpMeterId = _cursor.getString(_cursorIndexOfMeterId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpRecordedAt;
            _tmpRecordedAt = _cursor.getString(_cursorIndexOfRecordedAt);
            final Integer _tmpVt;
            if (_cursor.isNull(_cursorIndexOfVt)) {
              _tmpVt = null;
            } else {
              _tmpVt = _cursor.getInt(_cursorIndexOfVt);
            }
            final Integer _tmpMt;
            if (_cursor.isNull(_cursorIndexOfMt)) {
              _tmpMt = null;
            } else {
              _tmpMt = _cursor.getInt(_cursorIndexOfMt);
            }
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            final String _tmpConfidence;
            if (_cursor.isNull(_cursorIndexOfConfidence)) {
              _tmpConfidence = null;
            } else {
              _tmpConfidence = _cursor.getString(_cursorIndexOfConfidence);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _item = new ReadingEntity(_tmpId,_tmpMeterId,_tmpUserId,_tmpRecordedAt,_tmpVt,_tmpMt,_tmpSource,_tmpConfidence,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLatestByMeterId(final String meterId,
      final Continuation<? super ReadingEntity> $completion) {
    final String _sql = "SELECT * FROM readings WHERE meterId = ? ORDER BY recordedAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, meterId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReadingEntity>() {
      @Override
      @Nullable
      public ReadingEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "meterId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfRecordedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedAt");
          final int _cursorIndexOfVt = CursorUtil.getColumnIndexOrThrow(_cursor, "vt");
          final int _cursorIndexOfMt = CursorUtil.getColumnIndexOrThrow(_cursor, "mt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final ReadingEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMeterId;
            _tmpMeterId = _cursor.getString(_cursorIndexOfMeterId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpRecordedAt;
            _tmpRecordedAt = _cursor.getString(_cursorIndexOfRecordedAt);
            final Integer _tmpVt;
            if (_cursor.isNull(_cursorIndexOfVt)) {
              _tmpVt = null;
            } else {
              _tmpVt = _cursor.getInt(_cursorIndexOfVt);
            }
            final Integer _tmpMt;
            if (_cursor.isNull(_cursorIndexOfMt)) {
              _tmpMt = null;
            } else {
              _tmpMt = _cursor.getInt(_cursorIndexOfMt);
            }
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            final String _tmpConfidence;
            if (_cursor.isNull(_cursorIndexOfConfidence)) {
              _tmpConfidence = null;
            } else {
              _tmpConfidence = _cursor.getString(_cursorIndexOfConfidence);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _result = new ReadingEntity(_tmpId,_tmpMeterId,_tmpUserId,_tmpRecordedAt,_tmpVt,_tmpMt,_tmpSource,_tmpConfidence,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getByMeterIdList(final String meterId,
      final Continuation<? super List<ReadingEntity>> $completion) {
    final String _sql = "SELECT * FROM readings WHERE meterId = ? ORDER BY recordedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, meterId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReadingEntity>>() {
      @Override
      @NonNull
      public List<ReadingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "meterId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfRecordedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedAt");
          final int _cursorIndexOfVt = CursorUtil.getColumnIndexOrThrow(_cursor, "vt");
          final int _cursorIndexOfMt = CursorUtil.getColumnIndexOrThrow(_cursor, "mt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ReadingEntity> _result = new ArrayList<ReadingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReadingEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMeterId;
            _tmpMeterId = _cursor.getString(_cursorIndexOfMeterId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpRecordedAt;
            _tmpRecordedAt = _cursor.getString(_cursorIndexOfRecordedAt);
            final Integer _tmpVt;
            if (_cursor.isNull(_cursorIndexOfVt)) {
              _tmpVt = null;
            } else {
              _tmpVt = _cursor.getInt(_cursorIndexOfVt);
            }
            final Integer _tmpMt;
            if (_cursor.isNull(_cursorIndexOfMt)) {
              _tmpMt = null;
            } else {
              _tmpMt = _cursor.getInt(_cursorIndexOfMt);
            }
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            final String _tmpConfidence;
            if (_cursor.isNull(_cursorIndexOfConfidence)) {
              _tmpConfidence = null;
            } else {
              _tmpConfidence = _cursor.getString(_cursorIndexOfConfidence);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _item = new ReadingEntity(_tmpId,_tmpMeterId,_tmpUserId,_tmpRecordedAt,_tmpVt,_tmpMt,_tmpSource,_tmpConfidence,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
