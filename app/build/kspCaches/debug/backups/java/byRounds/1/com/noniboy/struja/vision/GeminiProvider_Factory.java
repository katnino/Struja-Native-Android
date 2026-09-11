package com.noniboy.struja.vision;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class GeminiProvider_Factory implements Factory<GeminiProvider> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public GeminiProvider_Factory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public GeminiProvider get() {
    return newInstance(okHttpClientProvider.get());
  }

  public static GeminiProvider_Factory create(Provider<OkHttpClient> okHttpClientProvider) {
    return new GeminiProvider_Factory(okHttpClientProvider);
  }

  public static GeminiProvider newInstance(OkHttpClient okHttpClient) {
    return new GeminiProvider(okHttpClient);
  }
}
