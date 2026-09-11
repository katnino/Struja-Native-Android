package com.noniboy.struja.ui.screens.reading;

import androidx.lifecycle.SavedStateHandle;
import com.noniboy.struja.data.repository.BillRepository;
import com.noniboy.struja.data.repository.MeterRepository;
import com.noniboy.struja.data.repository.ReadingRepository;
import com.noniboy.struja.vision.VisionExtractor;
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
public final class ReadingViewModel_Factory implements Factory<ReadingViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<MeterRepository> meterRepositoryProvider;

  private final Provider<ReadingRepository> readingRepositoryProvider;

  private final Provider<BillRepository> billRepositoryProvider;

  private final Provider<VisionExtractor> visionExtractorProvider;

  public ReadingViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MeterRepository> meterRepositoryProvider,
      Provider<ReadingRepository> readingRepositoryProvider,
      Provider<BillRepository> billRepositoryProvider,
      Provider<VisionExtractor> visionExtractorProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.meterRepositoryProvider = meterRepositoryProvider;
    this.readingRepositoryProvider = readingRepositoryProvider;
    this.billRepositoryProvider = billRepositoryProvider;
    this.visionExtractorProvider = visionExtractorProvider;
  }

  @Override
  public ReadingViewModel get() {
    return newInstance(savedStateHandleProvider.get(), meterRepositoryProvider.get(), readingRepositoryProvider.get(), billRepositoryProvider.get(), visionExtractorProvider.get());
  }

  public static ReadingViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MeterRepository> meterRepositoryProvider,
      Provider<ReadingRepository> readingRepositoryProvider,
      Provider<BillRepository> billRepositoryProvider,
      Provider<VisionExtractor> visionExtractorProvider) {
    return new ReadingViewModel_Factory(savedStateHandleProvider, meterRepositoryProvider, readingRepositoryProvider, billRepositoryProvider, visionExtractorProvider);
  }

  public static ReadingViewModel newInstance(SavedStateHandle savedStateHandle,
      MeterRepository meterRepository, ReadingRepository readingRepository,
      BillRepository billRepository, VisionExtractor visionExtractor) {
    return new ReadingViewModel(savedStateHandle, meterRepository, readingRepository, billRepository, visionExtractor);
  }
}
