package com.noniboy.struja.data.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.noniboy.struja.data.db.entity.MeterEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class MeterDao_Impl implements MeterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MeterEntity> __insertionAdapterOfMeterEntity;

  private final EntityDeletionOrUpdateAdapter<MeterEntity> __deletionAdapterOfMeterEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public MeterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMeterEntity = new EntityInsertionAdapter<MeterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `meters` (`id`,`userId`,`name`,`tariffGroup`,`approvedKw`,`notes`,`createdAt`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MeterEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getUserId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getTariffGroup());
        statement.bindDouble(5, entity.getApprovedKw());
        if (entity.getNotes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNotes());
        }
        statement.bindString(7, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfMeterEntity = new EntityDeletionOrUpdateAdapter<MeterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `meters` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MeterEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM meters WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MeterEntity meter, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMeterEntity.insert(meter);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final MeterEntity meter, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMeterEntity.handle(meter);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MeterEntity>> getAll() {
    final String _sql = "SELECT * FROM meters ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"meters"}, new Callable<List<MeterEntity>>() {
      @Override
      @NonNull
      public List<MeterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTariffGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "tariffGroup");
          final int _cursorIndexOfApprovedKw = CursorUtil.getColumnIndexOrThrow(_cursor, "approvedKw");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MeterEntity> _result = new ArrayList<MeterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MeterEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpTariffGroup;
            _tmpTariffGroup = _cursor.getString(_cursorIndexOfTariffGroup);
            final double _tmpApprovedKw;
            _tmpApprovedKw = _cursor.getDouble(_cursorIndexOfApprovedKw);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _item = new MeterEntity(_tmpId,_tmpUserId,_tmpName,_tmpTariffGroup,_tmpApprovedKw,_tmpNotes,_tmpCreatedAt);
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
  public Object getAllList(final Continuation<? super List<MeterEntity>> $completion) {
    final String _sql = "SELECT * FROM meters ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MeterEntity>>() {
      @Override
      @NonNull
      public List<MeterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTariffGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "tariffGroup");
          final int _cursorIndexOfApprovedKw = CursorUtil.getColumnIndexOrThrow(_cursor, "approvedKw");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MeterEntity> _result = new ArrayList<MeterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MeterEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpTariffGroup;
            _tmpTariffGroup = _cursor.getString(_cursorIndexOfTariffGroup);
            final double _tmpApprovedKw;
            _tmpApprovedKw = _cursor.getDouble(_cursorIndexOfApprovedKw);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _item = new MeterEntity(_tmpId,_tmpUserId,_tmpName,_tmpTariffGroup,_tmpApprovedKw,_tmpNotes,_tmpCreatedAt);
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

  @Override
  public Object getById(final String id, final Continuation<? super MeterEntity> $completion) {
    final String _sql = "SELECT * FROM meters WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MeterEntity>() {
      @Override
      @Nullable
      public MeterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTariffGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "tariffGroup");
          final int _cursorIndexOfApprovedKw = CursorUtil.getColumnIndexOrThrow(_cursor, "approvedKw");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final MeterEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpTariffGroup;
            _tmpTariffGroup = _cursor.getString(_cursorIndexOfTariffGroup);
            final double _tmpApprovedKw;
            _tmpApprovedKw = _cursor.getDouble(_cursorIndexOfApprovedKw);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _result = new MeterEntity(_tmpId,_tmpUserId,_tmpName,_tmpTariffGroup,_tmpApprovedKw,_tmpNotes,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
