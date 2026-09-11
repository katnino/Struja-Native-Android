package com.noniboy.struja.di;

import com.noniboy.struja.data.db.StrujaDatabase;
import com.noniboy.struja.data.db.dao.BillDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DatabaseModule_ProvideBillDaoFactory implements Factory<BillDao> {
  private final Provider<StrujaDatabase> databaseProvider;

  public DatabaseModule_ProvideBillDaoFactory(Provider<StrujaDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BillDao get() {
    return provideBillDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideBillDaoFactory create(
      Provider<StrujaDatabase> databaseProvider) {
    return new DatabaseModule_ProvideBillDaoFactory(databaseProvider);
  }

  public static BillDao provideBillDao(StrujaDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideBillDao(database));
  }
}
