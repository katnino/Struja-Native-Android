package com.noniboy.struja.di;

import com.noniboy.struja.data.db.dao.MeterDao;
import com.noniboy.struja.data.repository.MeterRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class RepositoryModule_ProvideMeterRepositoryFactory implements Factory<MeterRepository> {
  private final Provider<MeterDao> meterDaoProvider;

  public RepositoryModule_ProvideMeterRepositoryFactory(Provider<MeterDao> meterDaoProvider) {
    this.meterDaoProvider = meterDaoProvider;
  }

  @Override
  public MeterRepository get() {
    return provideMeterRepository(meterDaoProvider.get());
  }

  public static RepositoryModule_ProvideMeterRepositoryFactory create(
      Provider<MeterDao> meterDaoProvider) {
    return new RepositoryModule_ProvideMeterRepositoryFactory(meterDaoProvider);
  }

  public static MeterRepository provideMeterRepository(MeterDao meterDao) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideMeterRepository(meterDao));
  }
}
