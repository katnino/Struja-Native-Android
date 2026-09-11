package com.noniboy.struja.data.repository;

import com.noniboy.struja.data.db.dao.MeterDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class MeterRepository_Factory implements Factory<MeterRepository> {
  private final Provider<MeterDao> meterDaoProvider;

  public MeterRepository_Factory(Provider<MeterDao> meterDaoProvider) {
    this.meterDaoProvider = meterDaoProvider;
  }

  @Override
  public MeterRepository get() {
    return newInstance(meterDaoProvider.get());
  }

  public static MeterRepository_Factory create(Provider<MeterDao> meterDaoProvider) {
    return new MeterRepository_Factory(meterDaoProvider);
  }

  public static MeterRepository newInstance(MeterDao meterDao) {
    return new MeterRepository(meterDao);
  }
}
