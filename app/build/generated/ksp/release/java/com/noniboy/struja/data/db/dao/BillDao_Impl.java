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
import com.noniboy.struja.data.db.entity.BillEntity;
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
public final class BillDao_Impl implements BillDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BillEntity> __insertionAdapterOfBillEntity;

  public BillDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBillEntity = new EntityInsertionAdapter<BillEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bills` (`id`,`meterId`,`userId`,`periodStart`,`periodEnd`,`prevReadingId`,`currReadingId`,`approvedKw`,`consumptionKwh`,`mjernoMjesto`,`obracunskaSnaga`,`energyCost`,`transmissionBaseCost`,`totalTransmission`,`distributionBaseCost`,`totalDistribution`,`oieCost`,`subtotal`,`vatAmount`,`total`,`blocksJson`,`isPartial`,`createdAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BillEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getMeterId());
        statement.bindString(3, entity.getUserId());
        statement.bindString(4, entity.getPeriodStart());
        statement.bindString(5, entity.getPeriodEnd());
        if (entity.getPrevReadingId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPrevReadingId());
        }
        if (entity.getCurrReadingId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCurrReadingId());
        }
        statement.bindDouble(8, entity.getApprovedKw());
        statement.bindDouble(9, entity.getConsumptionKwh());
        statement.bindDouble(10, entity.getMjernoMjesto());
        statement.bindDouble(11, entity.getObracunskaSnaga());
        statement.bindDouble(12, entity.getEnergyCost());
        statement.bindDouble(13, entity.getTransmissionBaseCost());
        statement.bindDouble(14, entity.getTotalTransmission());
        statement.bindDouble(15, entity.getDistributionBaseCost());
        statement.bindDouble(16, entity.getTotalDistribution());
        statement.bindDouble(17, entity.getOieCost());
        statement.bindDouble(18, entity.getSubtotal());
        statement.bindDouble(19, entity.getVatAmount());
        statement.bindDouble(20, entity.getTotal());
        statement.bindString(21, entity.getBlocksJson());
        final int _tmp = entity.isPartial() ? 1 : 0;
        statement.bindLong(22, _tmp);
        statement.bindString(23, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object insert(final BillEntity bill, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBillEntity.insert(bill);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BillEntity>> getByMeterId(final String meterId) {
    final String _sql = "SELECT * FROM bills WHERE meterId = ? ORDER BY periodEnd DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, meterId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bills"}, new Callable<List<BillEntity>>() {
      @Override
      @NonNull
      public List<BillEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "meterId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfPrevReadingId = CursorUtil.getColumnIndexOrThrow(_cursor, "prevReadingId");
          final int _cursorIndexOfCurrReadingId = CursorUtil.getColumnIndexOrThrow(_cursor, "currReadingId");
          final int _cursorIndexOfApprovedKw = CursorUtil.getColumnIndexOrThrow(_cursor, "approvedKw");
          final int _cursorIndexOfConsumptionKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "consumptionKwh");
          final int _cursorIndexOfMjernoMjesto = CursorUtil.getColumnIndexOrThrow(_cursor, "mjernoMjesto");
          final int _cursorIndexOfObracunskaSnaga = CursorUtil.getColumnIndexOrThrow(_cursor, "obracunskaSnaga");
          final int _cursorIndexOfEnergyCost = CursorUtil.getColumnIndexOrThrow(_cursor, "energyCost");
          final int _cursorIndexOfTransmissionBaseCost = CursorUtil.getColumnIndexOrThrow(_cursor, "transmissionBaseCost");
          final int _cursorIndexOfTotalTransmission = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTransmission");
          final int _cursorIndexOfDistributionBaseCost = CursorUtil.getColumnIndexOrThrow(_cursor, "distributionBaseCost");
          final int _cursorIndexOfTotalDistribution = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDistribution");
          final int _cursorIndexOfOieCost = CursorUtil.getColumnIndexOrThrow(_cursor, "oieCost");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfVatAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "vatAmount");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfBlocksJson = CursorUtil.getColumnIndexOrThrow(_cursor, "blocksJson");
          final int _cursorIndexOfIsPartial = CursorUtil.getColumnIndexOrThrow(_cursor, "isPartial");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<BillEntity> _result = new ArrayList<BillEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BillEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMeterId;
            _tmpMeterId = _cursor.getString(_cursorIndexOfMeterId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpPeriodStart;
            _tmpPeriodStart = _cursor.getString(_cursorIndexOfPeriodStart);
            final String _tmpPeriodEnd;
            _tmpPeriodEnd = _cursor.getString(_cursorIndexOfPeriodEnd);
            final String _tmpPrevReadingId;
            if (_cursor.isNull(_cursorIndexOfPrevReadingId)) {
              _tmpPrevReadingId = null;
            } else {
              _tmpPrevReadingId = _cursor.getString(_cursorIndexOfPrevReadingId);
            }
            final String _tmpCurrReadingId;
            if (_cursor.isNull(_cursorIndexOfCurrReadingId)) {
              _tmpCurrReadingId = null;
            } else {
              _tmpCurrReadingId = _cursor.getString(_cursorIndexOfCurrReadingId);
            }
            final double _tmpApprovedKw;
            _tmpApprovedKw = _cursor.getDouble(_cursorIndexOfApprovedKw);
            final double _tmpConsumptionKwh;
            _tmpConsumptionKwh = _cursor.getDouble(_cursorIndexOfConsumptionKwh);
            final double _tmpMjernoMjesto;
            _tmpMjernoMjesto = _cursor.getDouble(_cursorIndexOfMjernoMjesto);
            final double _tmpObracunskaSnaga;
            _tmpObracunskaSnaga = _cursor.getDouble(_cursorIndexOfObracunskaSnaga);
            final double _tmpEnergyCost;
            _tmpEnergyCost = _cursor.getDouble(_cursorIndexOfEnergyCost);
            final double _tmpTransmissionBaseCost;
            _tmpTransmissionBaseCost = _cursor.getDouble(_cursorIndexOfTransmissionBaseCost);
            final double _tmpTotalTransmission;
            _tmpTotalTransmission = _cursor.getDouble(_cursorIndexOfTotalTransmission);
            final double _tmpDistributionBaseCost;
            _tmpDistributionBaseCost = _cursor.getDouble(_cursorIndexOfDistributionBaseCost);
            final double _tmpTotalDistribution;
            _tmpTotalDistribution = _cursor.getDouble(_cursorIndexOfTotalDistribution);
            final double _tmpOieCost;
            _tmpOieCost = _cursor.getDouble(_cursorIndexOfOieCost);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpVatAmount;
            _tmpVatAmount = _cursor.getDouble(_cursorIndexOfVatAmount);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpBlocksJson;
            _tmpBlocksJson = _cursor.getString(_cursorIndexOfBlocksJson);
            final boolean _tmpIsPartial;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPartial);
            _tmpIsPartial = _tmp != 0;
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _item = new BillEntity(_tmpId,_tmpMeterId,_tmpUserId,_tmpPeriodStart,_tmpPeriodEnd,_tmpPrevReadingId,_tmpCurrReadingId,_tmpApprovedKw,_tmpConsumptionKwh,_tmpMjernoMjesto,_tmpObracunskaSnaga,_tmpEnergyCost,_tmpTransmissionBaseCost,_tmpTotalTransmission,_tmpDistributionBaseCost,_tmpTotalDistribution,_tmpOieCost,_tmpSubtotal,_tmpVatAmount,_tmpTotal,_tmpBlocksJson,_tmpIsPartial,_tmpCreatedAt);
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
  public Object getByMeterIdList(final String meterId,
      final Continuation<? super List<BillEntity>> $completion) {
    final String _sql = "SELECT * FROM bills WHERE meterId = ? ORDER BY periodEnd DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, meterId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BillEntity>>() {
      @Override
      @NonNull
      public List<BillEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "meterId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfPrevReadingId = CursorUtil.getColumnIndexOrThrow(_cursor, "prevReadingId");
          final int _cursorIndexOfCurrReadingId = CursorUtil.getColumnIndexOrThrow(_cursor, "currReadingId");
          final int _cursorIndexOfApprovedKw = CursorUtil.getColumnIndexOrThrow(_cursor, "approvedKw");
          final int _cursorIndexOfConsumptionKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "consumptionKwh");
          final int _cursorIndexOfMjernoMjesto = CursorUtil.getColumnIndexOrThrow(_cursor, "mjernoMjesto");
          final int _cursorIndexOfObracunskaSnaga = CursorUtil.getColumnIndexOrThrow(_cursor, "obracunskaSnaga");
          final int _cursorIndexOfEnergyCost = CursorUtil.getColumnIndexOrThrow(_cursor, "energyCost");
          final int _cursorIndexOfTransmissionBaseCost = CursorUtil.getColumnIndexOrThrow(_cursor, "transmissionBaseCost");
          final int _cursorIndexOfTotalTransmission = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTransmission");
          final int _cursorIndexOfDistributionBaseCost = CursorUtil.getColumnIndexOrThrow(_cursor, "distributionBaseCost");
          final int _cursorIndexOfTotalDistribution = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDistribution");
          final int _cursorIndexOfOieCost = CursorUtil.getColumnIndexOrThrow(_cursor, "oieCost");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfVatAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "vatAmount");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfBlocksJson = CursorUtil.getColumnIndexOrThrow(_cursor, "blocksJson");
          final int _cursorIndexOfIsPartial = CursorUtil.getColumnIndexOrThrow(_cursor, "isPartial");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<BillEntity> _result = new ArrayList<BillEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BillEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMeterId;
            _tmpMeterId = _cursor.getString(_cursorIndexOfMeterId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpPeriodStart;
            _tmpPeriodStart = _cursor.getString(_cursorIndexOfPeriodStart);
            final String _tmpPeriodEnd;
            _tmpPeriodEnd = _cursor.getString(_cursorIndexOfPeriodEnd);
            final String _tmpPrevReadingId;
            if (_cursor.isNull(_cursorIndexOfPrevReadingId)) {
              _tmpPrevReadingId = null;
            } else {
              _tmpPrevReadingId = _cursor.getString(_cursorIndexOfPrevReadingId);
            }
            final String _tmpCurrReadingId;
            if (_cursor.isNull(_cursorIndexOfCurrReadingId)) {
              _tmpCurrReadingId = null;
            } else {
              _tmpCurrReadingId = _cursor.getString(_cursorIndexOfCurrReadingId);
            }
            final double _tmpApprovedKw;
            _tmpApprovedKw = _cursor.getDouble(_cursorIndexOfApprovedKw);
            final double _tmpConsumptionKwh;
            _tmpConsumptionKwh = _cursor.getDouble(_cursorIndexOfConsumptionKwh);
            final double _tmpMjernoMjesto;
            _tmpMjernoMjesto = _cursor.getDouble(_cursorIndexOfMjernoMjesto);
            final double _tmpObracunskaSnaga;
            _tmpObracunskaSnaga = _cursor.getDouble(_cursorIndexOfObracunskaSnaga);
            final double _tmpEnergyCost;
            _tmpEnergyCost = _cursor.getDouble(_cursorIndexOfEnergyCost);
            final double _tmpTransmissionBaseCost;
            _tmpTransmissionBaseCost = _cursor.getDouble(_cursorIndexOfTransmissionBaseCost);
            final double _tmpTotalTransmission;
            _tmpTotalTransmission = _cursor.getDouble(_cursorIndexOfTotalTransmission);
            final double _tmpDistributionBaseCost;
            _tmpDistributionBaseCost = _cursor.getDouble(_cursorIndexOfDistributionBaseCost);
            final double _tmpTotalDistribution;
            _tmpTotalDistribution = _cursor.getDouble(_cursorIndexOfTotalDistribution);
            final double _tmpOieCost;
            _tmpOieCost = _cursor.getDouble(_cursorIndexOfOieCost);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpVatAmount;
            _tmpVatAmount = _cursor.getDouble(_cursorIndexOfVatAmount);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpBlocksJson;
            _tmpBlocksJson = _cursor.getString(_cursorIndexOfBlocksJson);
            final boolean _tmpIsPartial;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPartial);
            _tmpIsPartial = _tmp != 0;
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _item = new BillEntity(_tmpId,_tmpMeterId,_tmpUserId,_tmpPeriodStart,_tmpPeriodEnd,_tmpPrevReadingId,_tmpCurrReadingId,_tmpApprovedKw,_tmpConsumptionKwh,_tmpMjernoMjesto,_tmpObracunskaSnaga,_tmpEnergyCost,_tmpTransmissionBaseCost,_tmpTotalTransmission,_tmpDistributionBaseCost,_tmpTotalDistribution,_tmpOieCost,_tmpSubtotal,_tmpVatAmount,_tmpTotal,_tmpBlocksJson,_tmpIsPartial,_tmpCreatedAt);
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
  public Object getById(final String id, final Continuation<? super BillEntity> $completion) {
    final String _sql = "SELECT * FROM bills WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BillEntity>() {
      @Override
      @Nullable
      public BillEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "meterId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfPrevReadingId = CursorUtil.getColumnIndexOrThrow(_cursor, "prevReadingId");
          final int _cursorIndexOfCurrReadingId = CursorUtil.getColumnIndexOrThrow(_cursor, "currReadingId");
          final int _cursorIndexOfApprovedKw = CursorUtil.getColumnIndexOrThrow(_cursor, "approvedKw");
          final int _cursorIndexOfConsumptionKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "consumptionKwh");
          final int _cursorIndexOfMjernoMjesto = CursorUtil.getColumnIndexOrThrow(_cursor, "mjernoMjesto");
          final int _cursorIndexOfObracunskaSnaga = CursorUtil.getColumnIndexOrThrow(_cursor, "obracunskaSnaga");
          final int _cursorIndexOfEnergyCost = CursorUtil.getColumnIndexOrThrow(_cursor, "energyCost");
          final int _cursorIndexOfTransmissionBaseCost = CursorUtil.getColumnIndexOrThrow(_cursor, "transmissionBaseCost");
          final int _cursorIndexOfTotalTransmission = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTransmission");
          final int _cursorIndexOfDistributionBaseCost = CursorUtil.getColumnIndexOrThrow(_cursor, "distributionBaseCost");
          final int _cursorIndexOfTotalDistribution = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDistribution");
          final int _cursorIndexOfOieCost = CursorUtil.getColumnIndexOrThrow(_cursor, "oieCost");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfVatAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "vatAmount");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfBlocksJson = CursorUtil.getColumnIndexOrThrow(_cursor, "blocksJson");
          final int _cursorIndexOfIsPartial = CursorUtil.getColumnIndexOrThrow(_cursor, "isPartial");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final BillEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMeterId;
            _tmpMeterId = _cursor.getString(_cursorIndexOfMeterId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpPeriodStart;
            _tmpPeriodStart = _cursor.getString(_cursorIndexOfPeriodStart);
            final String _tmpPeriodEnd;
            _tmpPeriodEnd = _cursor.getString(_cursorIndexOfPeriodEnd);
            final String _tmpPrevReadingId;
            if (_cursor.isNull(_cursorIndexOfPrevReadingId)) {
              _tmpPrevReadingId = null;
            } else {
              _tmpPrevReadingId = _cursor.getString(_cursorIndexOfPrevReadingId);
            }
            final String _tmpCurrReadingId;
            if (_cursor.isNull(_cursorIndexOfCurrReadingId)) {
              _tmpCurrReadingId = null;
            } else {
              _tmpCurrReadingId = _cursor.getString(_cursorIndexOfCurrReadingId);
            }
            final double _tmpApprovedKw;
            _tmpApprovedKw = _cursor.getDouble(_cursorIndexOfApprovedKw);
            final double _tmpConsumptionKwh;
            _tmpConsumptionKwh = _cursor.getDouble(_cursorIndexOfConsumptionKwh);
            final double _tmpMjernoMjesto;
            _tmpMjernoMjesto = _cursor.getDouble(_cursorIndexOfMjernoMjesto);
            final double _tmpObracunskaSnaga;
            _tmpObracunskaSnaga = _cursor.getDouble(_cursorIndexOfObracunskaSnaga);
            final double _tmpEnergyCost;
            _tmpEnergyCost = _cursor.getDouble(_cursorIndexOfEnergyCost);
            final double _tmpTransmissionBaseCost;
            _tmpTransmissionBaseCost = _cursor.getDouble(_cursorIndexOfTransmissionBaseCost);
            final double _tmpTotalTransmission;
            _tmpTotalTransmission = _cursor.getDouble(_cursorIndexOfTotalTransmission);
            final double _tmpDistributionBaseCost;
            _tmpDistributionBaseCost = _cursor.getDouble(_cursorIndexOfDistributionBaseCost);
            final double _tmpTotalDistribution;
            _tmpTotalDistribution = _cursor.getDouble(_cursorIndexOfTotalDistribution);
            final double _tmpOieCost;
            _tmpOieCost = _cursor.getDouble(_cursorIndexOfOieCost);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpVatAmount;
            _tmpVatAmount = _cursor.getDouble(_cursorIndexOfVatAmount);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpBlocksJson;
            _tmpBlocksJson = _cursor.getString(_cursorIndexOfBlocksJson);
            final boolean _tmpIsPartial;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPartial);
            _tmpIsPartial = _tmp != 0;
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            _result = new BillEntity(_tmpId,_tmpMeterId,_tmpUserId,_tmpPeriodStart,_tmpPeriodEnd,_tmpPrevReadingId,_tmpCurrReadingId,_tmpApprovedKw,_tmpConsumptionKwh,_tmpMjernoMjesto,_tmpObracunskaSnaga,_tmpEnergyCost,_tmpTransmissionBaseCost,_tmpTotalTransmission,_tmpDistributionBaseCost,_tmpTotalDistribution,_tmpOieCost,_tmpSubtotal,_tmpVatAmount,_tmpTotal,_tmpBlocksJson,_tmpIsPartial,_tmpCreatedAt);
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
