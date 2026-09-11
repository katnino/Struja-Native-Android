package com.noniboy.struja.data.repository;

import com.google.gson.Gson;
import com.noniboy.struja.data.db.dao.BillDao;
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
public final class BillRepository_Factory implements Factory<BillRepository> {
  private final Provider<BillDao> billDaoProvider;

  private final Provider<Gson> gsonProvider;

  public BillRepository_Factory(Provider<BillDao> billDaoProvider, Provider<Gson> gsonProvider) {
    this.billDaoProvider = billDaoProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public BillRepository get() {
    return newInstance(billDaoProvider.get(), gsonProvider.get());
  }

  public static BillRepository_Factory create(Provider<BillDao> billDaoProvider,
      Provider<Gson> gsonProvider) {
    return new BillRepository_Factory(billDaoProvider, gsonProvider);
  }

  public static BillRepository newInstance(BillDao billDao, Gson gson) {
    return new BillRepository(billDao, gson);
  }
}
