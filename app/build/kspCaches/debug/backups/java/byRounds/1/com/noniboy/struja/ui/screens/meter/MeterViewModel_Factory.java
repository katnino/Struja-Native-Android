package com.noniboy.struja.ui.screens.meter;

import androidx.lifecycle.SavedStateHandle;
import com.noniboy.struja.data.repository.BillRepository;
import com.noniboy.struja.data.repository.MeterRepository;
import com.noniboy.struja.data.repository.ReadingRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class MeterViewModel_Factory implements Factory<MeterViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<MeterRepository> meterRepositoryProvider;

  private final Provider<ReadingRepository> readingRepositoryProvider;

  private final Provider<BillRepository> billRepositoryProvider;

  public MeterViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MeterRepository> meterRepositoryProvider,
      Provider<ReadingRepository> readingRepositoryProvider,
      Provider<BillRepository> billRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.meterRepositoryProvider = meterRepositoryProvider;
    this.readingRepositoryProvider = readingRepositoryProvider;
    this.billRepositoryProvider = billRepositoryProvider;
  }

  @Override
  public MeterViewModel get() {
    return newInstance(savedStateHandleProvider.get(), meterRepositoryProvider.get(), readingRepositoryProvider.get(), billRepositoryProvider.get());
  }

  public static MeterViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MeterRepository> meterRepositoryProvider,
      Provider<ReadingRepository> readingRepositoryProvider,
      Provider<BillRepository> billRepositoryProvider) {
    return new MeterViewModel_Factory(savedStateHandleProvider, meterRepositoryProvider, readingRepositoryProvider, billRepositoryProvider);
  }

  public static MeterViewModel newInstance(SavedStateHandle savedStateHandle,
      MeterRepository meterRepository, ReadingRepository readingRepository,
      BillRepository billRepository) {
    return new MeterViewModel(savedStateHandle, meterRepository, readingRepository, billRepository);
  }
}
