package com.noniboy.struja.data.backup;

import com.google.gson.Gson;
import com.noniboy.struja.data.db.StrujaDatabase;
import com.noniboy.struja.data.repository.BillRepository;
import com.noniboy.struja.data.repository.MeterRepository;
import com.noniboy.struja.data.repository.ReadingRepository;
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
public final class BackupRepository_Factory implements Factory<BackupRepository> {
  private final Provider<StrujaDatabase> databaseProvider;

  private final Provider<MeterRepository> meterRepositoryProvider;

  private final Provider<ReadingRepository> readingRepositoryProvider;

  private final Provider<BillRepository> billRepositoryProvider;

  private final Provider<Gson> gsonProvider;

  public BackupRepository_Factory(Provider<StrujaDatabase> databaseProvider,
      Provider<MeterRepository> meterRepositoryProvider,
      Provider<ReadingRepository> readingRepositoryProvider,
      Provider<BillRepository> billRepositoryProvider, Provider<Gson> gsonProvider) {
    this.databaseProvider = databaseProvider;
    this.meterRepositoryProvider = meterRepositoryProvider;
    this.readingRepositoryProvider = readingRepositoryProvider;
    this.billRepositoryProvider = billRepositoryProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public BackupRepository get() {
    return newInstance(databaseProvider.get(), meterRepositoryProvider.get(), readingRepositoryProvider.get(), billRepositoryProvider.get(), gsonProvider.get());
  }

  public static BackupRepository_Factory create(Provider<StrujaDatabase> databaseProvider,
      Provider<MeterRepository> meterRepositoryProvider,
      Provider<ReadingRepository> readingRepositoryProvider,
      Provider<BillRepository> billRepositoryProvider, Provider<Gson> gsonProvider) {
    return new BackupRepository_Factory(databaseProvider, meterRepositoryProvider, readingRepositoryProvider, billRepositoryProvider, gsonProvider);
  }

  public static BackupRepository newInstance(StrujaDatabase database,
      MeterRepository meterRepository, ReadingRepository readingRepository,
      BillRepository billRepository, Gson gson) {
    return new BackupRepository(database, meterRepository, readingRepository, billRepository, gson);
  }
}
