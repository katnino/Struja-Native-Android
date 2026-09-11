package com.noniboy.struja.vision;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class VisionExtractor_Factory implements Factory<VisionExtractor> {
  private final Provider<Context> contextProvider;

  private final Provider<GeminiProvider> geminiProvider;

  public VisionExtractor_Factory(Provider<Context> contextProvider,
      Provider<GeminiProvider> geminiProvider) {
    this.contextProvider = contextProvider;
    this.geminiProvider = geminiProvider;
  }

  @Override
  public VisionExtractor get() {
    return newInstance(contextProvider.get(), geminiProvider.get());
  }

  public static VisionExtractor_Factory create(Provider<Context> contextProvider,
      Provider<GeminiProvider> geminiProvider) {
    return new VisionExtractor_Factory(contextProvider, geminiProvider);
  }

  public static VisionExtractor newInstance(Context context, GeminiProvider geminiProvider) {
    return new VisionExtractor(context, geminiProvider);
  }
}
