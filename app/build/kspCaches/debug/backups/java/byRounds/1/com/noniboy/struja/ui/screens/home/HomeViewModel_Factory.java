package com.noniboy.struja.ui.screens.home;

import com.noniboy.struja.data.repository.MeterRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<MeterRepository> meterRepositoryProvider;

  public HomeViewModel_Factory(Provider<MeterRepository> meterRepositoryProvider) {
    this.meterRepositoryProvider = meterRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(meterRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<MeterRepository> meterRepositoryProvider) {
    return new HomeViewModel_Factory(meterRepositoryProvider);
  }

  public static HomeViewModel newInstance(MeterRepository meterRepository) {
    return new HomeViewModel(meterRepository);
  }
}
