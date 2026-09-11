package com.noniboy.struja.pdf;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class PdfGenerator_Factory implements Factory<PdfGenerator> {
  @Override
  public PdfGenerator get() {
    return newInstance();
  }

  public static PdfGenerator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PdfGenerator newInstance() {
    return new PdfGenerator();
  }

  private static final class InstanceHolder {
    private static final PdfGenerator_Factory INSTANCE = new PdfGenerator_Factory();
  }
}
