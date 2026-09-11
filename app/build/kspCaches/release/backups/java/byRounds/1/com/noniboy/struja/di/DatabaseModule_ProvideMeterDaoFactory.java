package com.noniboy.struja.di;

import com.noniboy.struja.data.db.StrujaDatabase;
import com.noniboy.struja.data.db.dao.MeterDao;
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
public final class DatabaseModule_ProvideMeterDaoFactory implements Factory<MeterDao> {
  private final Provider<StrujaDatabase> databaseProvider;

  public DatabaseModule_ProvideMeterDaoFactory(Provider<StrujaDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MeterDao get() {
    return provideMeterDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMeterDaoFactory create(
      Provider<StrujaDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMeterDaoFactory(databaseProvider);
  }

  public static MeterDao provideMeterDao(StrujaDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMeterDao(database));
  }
}
