package com.noniboy.struja.di;

import com.noniboy.struja.data.db.StrujaDatabase;
import com.noniboy.struja.data.db.dao.ReadingDao;
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
public final class DatabaseModule_ProvideReadingDaoFactory implements Factory<ReadingDao> {
  private final Provider<StrujaDatabase> databaseProvider;

  public DatabaseModule_ProvideReadingDaoFactory(Provider<StrujaDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ReadingDao get() {
    return provideReadingDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideReadingDaoFactory create(
      Provider<StrujaDatabase> databaseProvider) {
    return new DatabaseModule_ProvideReadingDaoFactory(databaseProvider);
  }

  public static ReadingDao provideReadingDao(StrujaDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideReadingDao(database));
  }
}
