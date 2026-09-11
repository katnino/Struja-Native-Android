package com.noniboy.struja.ui;

import android.content.Context;
import com.google.gson.Gson;
import com.noniboy.struja.data.backup.BackupRepository;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<BackupRepository> backupRepositoryProvider;

  private final Provider<Gson> gsonProvider;

  public SettingsViewModel_Factory(Provider<Context> contextProvider,
      Provider<BackupRepository> backupRepositoryProvider, Provider<Gson> gsonProvider) {
    this.contextProvider = contextProvider;
    this.backupRepositoryProvider = backupRepositoryProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(contextProvider.get(), backupRepositoryProvider.get(), gsonProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<BackupRepository> backupRepositoryProvider, Provider<Gson> gsonProvider) {
    return new SettingsViewModel_Factory(contextProvider, backupRepositoryProvider, gsonProvider);
  }

  public static SettingsViewModel newInstance(Context context, BackupRepository backupRepository,
      Gson gson) {
    return new SettingsViewModel(context, backupRepository, gson);
  }
}
