package com.noniboy.struja;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.noniboy.struja.data.backup.BackupRepository;
import com.noniboy.struja.data.db.StrujaDatabase;
import com.noniboy.struja.data.db.dao.BillDao;
import com.noniboy.struja.data.db.dao.MeterDao;
import com.noniboy.struja.data.db.dao.ReadingDao;
import com.noniboy.struja.data.repository.BillRepository;
import com.noniboy.struja.data.repository.MeterRepository;
import com.noniboy.struja.data.repository.ReadingRepository;
import com.noniboy.struja.di.DatabaseModule_ProvideBillDaoFactory;
import com.noniboy.struja.di.DatabaseModule_ProvideDatabaseFactory;
import com.noniboy.struja.di.DatabaseModule_ProvideMeterDaoFactory;
import com.noniboy.struja.di.DatabaseModule_ProvideReadingDaoFactory;
import com.noniboy.struja.di.NetworkModule_ProvideGsonFactory;
import com.noniboy.struja.di.NetworkModule_ProvideOkHttpClientFactory;
import com.noniboy.struja.di.RepositoryModule_ProvideBillRepositoryFactory;
import com.noniboy.struja.di.RepositoryModule_ProvideMeterRepositoryFactory;
import com.noniboy.struja.di.RepositoryModule_ProvideReadingRepositoryFactory;
import com.noniboy.struja.pdf.PdfGenerator;
import com.noniboy.struja.ui.SettingsViewModel;
import com.noniboy.struja.ui.SettingsViewModel_HiltModules;
import com.noniboy.struja.ui.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.noniboy.struja.ui.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.noniboy.struja.ui.screens.bill.BillViewModel;
import com.noniboy.struja.ui.screens.bill.BillViewModel_HiltModules;
import com.noniboy.struja.ui.screens.bill.BillViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.noniboy.struja.ui.screens.bill.BillViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.noniboy.struja.ui.screens.home.HomeViewModel;
import com.noniboy.struja.ui.screens.home.HomeViewModel_HiltModules;
import com.noniboy.struja.ui.screens.home.HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.noniboy.struja.ui.screens.home.HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.noniboy.struja.ui.screens.meter.MeterViewModel;
import com.noniboy.struja.ui.screens.meter.MeterViewModel_HiltModules;
import com.noniboy.struja.ui.screens.meter.MeterViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.noniboy.struja.ui.screens.meter.MeterViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.noniboy.struja.ui.screens.reading.ReadingViewModel;
import com.noniboy.struja.ui.screens.reading.ReadingViewModel_HiltModules;
import com.noniboy.struja.ui.screens.reading.ReadingViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.noniboy.struja.ui.screens.reading.ReadingViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.noniboy.struja.vision.GeminiProvider;
import com.noniboy.struja.vision.VisionExtractor;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class DaggerStrujaApp_HiltComponents_SingletonC {
  private DaggerStrujaApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public StrujaApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements StrujaApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public StrujaApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements StrujaApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public StrujaApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements StrujaApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public StrujaApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements StrujaApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public StrujaApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements StrujaApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public StrujaApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements StrujaApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public StrujaApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements StrujaApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public StrujaApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends StrujaApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends StrujaApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends StrujaApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends StrujaApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(5).put(BillViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, BillViewModel_HiltModules.KeyModule.provide()).put(HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, HomeViewModel_HiltModules.KeyModule.provide()).put(MeterViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, MeterViewModel_HiltModules.KeyModule.provide()).put(ReadingViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ReadingViewModel_HiltModules.KeyModule.provide()).put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends StrujaApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<BillViewModel> billViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<MeterViewModel> meterViewModelProvider;

    private Provider<ReadingViewModel> readingViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.billViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.meterViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.readingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(5).put(BillViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) billViewModelProvider)).put(HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) homeViewModelProvider)).put(MeterViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) meterViewModelProvider)).put(ReadingViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) readingViewModelProvider)).put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) settingsViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.noniboy.struja.ui.screens.bill.BillViewModel 
          return (T) new BillViewModel(viewModelCImpl.savedStateHandle, ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideMeterRepositoryProvider.get(), singletonCImpl.provideBillRepositoryProvider.get(), singletonCImpl.pdfGeneratorProvider.get());

          case 1: // com.noniboy.struja.ui.screens.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.provideMeterRepositoryProvider.get());

          case 2: // com.noniboy.struja.ui.screens.meter.MeterViewModel 
          return (T) new MeterViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.provideMeterRepositoryProvider.get(), singletonCImpl.provideReadingRepositoryProvider.get(), singletonCImpl.provideBillRepositoryProvider.get());

          case 3: // com.noniboy.struja.ui.screens.reading.ReadingViewModel 
          return (T) new ReadingViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.provideMeterRepositoryProvider.get(), singletonCImpl.provideReadingRepositoryProvider.get(), singletonCImpl.provideBillRepositoryProvider.get(), singletonCImpl.visionExtractorProvider.get());

          case 4: // com.noniboy.struja.ui.SettingsViewModel 
          return (T) new SettingsViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.backupRepositoryProvider.get(), singletonCImpl.provideGsonProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends StrujaApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends StrujaApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends StrujaApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<StrujaDatabase> provideDatabaseProvider;

    private Provider<MeterRepository> provideMeterRepositoryProvider;

    private Provider<Gson> provideGsonProvider;

    private Provider<BillRepository> provideBillRepositoryProvider;

    private Provider<PdfGenerator> pdfGeneratorProvider;

    private Provider<ReadingRepository> provideReadingRepositoryProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<GeminiProvider> geminiProvider;

    private Provider<VisionExtractor> visionExtractorProvider;

    private Provider<BackupRepository> backupRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private MeterDao meterDao() {
      return DatabaseModule_ProvideMeterDaoFactory.provideMeterDao(provideDatabaseProvider.get());
    }

    private BillDao billDao() {
      return DatabaseModule_ProvideBillDaoFactory.provideBillDao(provideDatabaseProvider.get());
    }

    private ReadingDao readingDao() {
      return DatabaseModule_ProvideReadingDaoFactory.provideReadingDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<StrujaDatabase>(singletonCImpl, 1));
      this.provideMeterRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<MeterRepository>(singletonCImpl, 0));
      this.provideGsonProvider = DoubleCheck.provider(new SwitchingProvider<Gson>(singletonCImpl, 3));
      this.provideBillRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<BillRepository>(singletonCImpl, 2));
      this.pdfGeneratorProvider = DoubleCheck.provider(new SwitchingProvider<PdfGenerator>(singletonCImpl, 4));
      this.provideReadingRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ReadingRepository>(singletonCImpl, 5));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 8));
      this.geminiProvider = DoubleCheck.provider(new SwitchingProvider<GeminiProvider>(singletonCImpl, 7));
      this.visionExtractorProvider = DoubleCheck.provider(new SwitchingProvider<VisionExtractor>(singletonCImpl, 6));
      this.backupRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<BackupRepository>(singletonCImpl, 9));
    }

    @Override
    public void injectStrujaApp(StrujaApp strujaApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.noniboy.struja.data.repository.MeterRepository 
          return (T) RepositoryModule_ProvideMeterRepositoryFactory.provideMeterRepository(singletonCImpl.meterDao());

          case 1: // com.noniboy.struja.data.db.StrujaDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.noniboy.struja.data.repository.BillRepository 
          return (T) RepositoryModule_ProvideBillRepositoryFactory.provideBillRepository(singletonCImpl.billDao(), singletonCImpl.provideGsonProvider.get());

          case 3: // com.google.gson.Gson 
          return (T) NetworkModule_ProvideGsonFactory.provideGson();

          case 4: // com.noniboy.struja.pdf.PdfGenerator 
          return (T) new PdfGenerator();

          case 5: // com.noniboy.struja.data.repository.ReadingRepository 
          return (T) RepositoryModule_ProvideReadingRepositoryFactory.provideReadingRepository(singletonCImpl.readingDao());

          case 6: // com.noniboy.struja.vision.VisionExtractor 
          return (T) new VisionExtractor(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.geminiProvider.get());

          case 7: // com.noniboy.struja.vision.GeminiProvider 
          return (T) new GeminiProvider(singletonCImpl.provideOkHttpClientProvider.get());

          case 8: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient();

          case 9: // com.noniboy.struja.data.backup.BackupRepository 
          return (T) new BackupRepository(singletonCImpl.provideDatabaseProvider.get(), singletonCImpl.provideMeterRepositoryProvider.get(), singletonCImpl.provideReadingRepositoryProvider.get(), singletonCImpl.provideBillRepositoryProvider.get(), singletonCImpl.provideGsonProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
