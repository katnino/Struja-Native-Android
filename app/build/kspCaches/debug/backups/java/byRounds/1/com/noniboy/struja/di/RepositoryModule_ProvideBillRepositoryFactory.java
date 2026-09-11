package com.noniboy.struja.di;

import com.google.gson.Gson;
import com.noniboy.struja.data.db.dao.BillDao;
import com.noniboy.struja.data.repository.BillRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class RepositoryModule_ProvideBillRepositoryFactory implements Factory<BillRepository> {
  private final Provider<BillDao> billDaoProvider;

  private final Provider<Gson> gsonProvider;

  public RepositoryModule_ProvideBillRepositoryFactory(Provider<BillDao> billDaoProvider,
      Provider<Gson> gsonProvider) {
    this.billDaoProvider = billDaoProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public BillRepository get() {
    return provideBillRepository(billDaoProvider.get(), gsonProvider.get());
  }

  public static RepositoryModule_ProvideBillRepositoryFactory create(
      Provider<BillDao> billDaoProvider, Provider<Gson> gsonProvider) {
    return new RepositoryModule_ProvideBillRepositoryFactory(billDaoProvider, gsonProvider);
  }

  public static BillRepository provideBillRepository(BillDao billDao, Gson gson) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideBillRepository(billDao, gson));
  }
}
