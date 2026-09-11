package com.noniboy.struja.data.repository;

import com.noniboy.struja.data.db.dao.ReadingDao;
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
public final class ReadingRepository_Factory implements Factory<ReadingRepository> {
  private final Provider<ReadingDao> readingDaoProvider;

  public ReadingRepository_Factory(Provider<ReadingDao> readingDaoProvider) {
    this.readingDaoProvider = readingDaoProvider;
  }

  @Override
  public ReadingRepository get() {
    return newInstance(readingDaoProvider.get());
  }

  public static ReadingRepository_Factory create(Provider<ReadingDao> readingDaoProvider) {
    return new ReadingRepository_Factory(readingDaoProvider);
  }

  public static ReadingRepository newInstance(ReadingDao readingDao) {
    return new ReadingRepository(readingDao);
  }
}
