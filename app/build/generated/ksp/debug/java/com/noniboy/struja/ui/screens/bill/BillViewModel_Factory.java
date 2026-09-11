package com.noniboy.struja.ui.screens.bill;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
import com.noniboy.struja.data.repository.BillRepository;
import com.noniboy.struja.data.repository.MeterRepository;
import com.noniboy.struja.pdf.PdfGenerator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class BillViewModel_Factory implements Factory<BillViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<Context> contextProvider;

  private final Provider<MeterRepository> meterRepositoryProvider;

  private final Provider<BillRepository> billRepositoryProvider;

  private final Provider<PdfGenerator> pdfGeneratorProvider;

  public BillViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<Context> contextProvider, Provider<MeterRepository> meterRepositoryProvider,
      Provider<BillRepository> billRepositoryProvider,
      Provider<PdfGenerator> pdfGeneratorProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.contextProvider = contextProvider;
    this.meterRepositoryProvider = meterRepositoryProvider;
    this.billRepositoryProvider = billRepositoryProvider;
    this.pdfGeneratorProvider = pdfGeneratorProvider;
  }

  @Override
  public BillViewModel get() {
    return newInstance(savedStateHandleProvider.get(), contextProvider.get(), meterRepositoryProvider.get(), billRepositoryProvider.get(), pdfGeneratorProvider.get());
  }

  public static BillViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<Context> contextProvider, Provider<MeterRepository> meterRepositoryProvider,
      Provider<BillRepository> billRepositoryProvider,
      Provider<PdfGenerator> pdfGeneratorProvider) {
    return new BillViewModel_Factory(savedStateHandleProvider, contextProvider, meterRepositoryProvider, billRepositoryProvider, pdfGeneratorProvider);
  }

  public static BillViewModel newInstance(SavedStateHandle savedStateHandle, Context context,
      MeterRepository meterRepository, BillRepository billRepository, PdfGenerator pdfGenerator) {
    return new BillViewModel(savedStateHandle, context, meterRepository, billRepository, pdfGenerator);
  }
}
