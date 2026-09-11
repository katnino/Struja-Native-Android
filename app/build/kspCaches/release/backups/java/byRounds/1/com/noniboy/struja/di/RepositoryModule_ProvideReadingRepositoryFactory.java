package com.noniboy.struja.di;

import com.noniboy.struja.data.db.dao.ReadingDao;
import com.noniboy.struja.data.repository.ReadingRepository;
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
public final class RepositoryModule_ProvideReadingRepositoryFactory implements Factory<ReadingRepository> {
  private final Provider<ReadingDao> readingDaoProvider;

  public RepositoryModule_ProvideReadingRepositoryFactory(Provider<ReadingDao> readingDaoProvider) {
    this.readingDaoProvider = readingDaoProvider;
  }

  @Override
  public ReadingRepository get() {
    return provideReadingRepository(readingDaoProvider.get());
  }

  public static RepositoryModule_ProvideReadingRepositoryFactory create(
      Provider<ReadingDao> readingDaoProvider) {
    return new RepositoryModule_ProvideReadingRepositoryFactory(readingDaoProvider);
  }

  public static ReadingRepository provideReadingRepository(ReadingDao readingDao) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideReadingRepository(readingDao));
  }
}
