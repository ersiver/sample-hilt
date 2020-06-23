# sample-hilt

This app was built following <b>Android Developers Codelabs</b>. The app demonstrates use of Hilt library.

### What is Hilt?
+ Hilt provides a standard way to do <b>DI injection</b> in your application by providing containers to every Android component in your project and managing the container's lifecycle automatically. This is done by leveraging the Dagger.

+ A container is a class which is in charge of providing dependencies in your codebase and knows how to create instances of other types of your app. It manages the graph of dependencies required to provide those instances by creating them and managing their lifecycle.

+ A container exposes methods to get instances of the types it provides. Those methods can return a different instance or the same instance. If the method always provides the same instance, we say that the type is <b>scoped to the container.</b>

+ Implementing dependency injection provides you with the following <b>advantages</b>:
<br>a) <b>Reusability</b> of code.
<br>b) Ease of <b>refactoring.</b>
<br>c) Ease of <b>testing.</b>

### @HiltAndroidApp Annotation
+ To create a <b>parent container</b> of dependencies that is attached to the <b>app's lifecycle</b>, we need to annotate the Application class with @HiltAndroidApp.

+  @HiltAndroidApp triggers Hilt's code generation. 

+ The application container is the parent container of the app, which means that <b>other containers can access the dependencies that it provides</b>.

### @AndroidEntryPoint

+ Annotating Android classes with @AndroidEntryPoint creates a dependencies container.

+ The container will be <b>attched</b> to that class and its lifecycle and will be able to inject instances to that class.

+ To provide instances we use use <b>field injection</b> -> we annotate the field we want to be provided (injected) with @Inject annotation.

+ Fields injected by Hilt cannot be private.

+ Under the hood, Hilt will populate fields annotaded with @Inject in the onAttach() lifecycle method with instances built in the <b>automatically generated LogsFragment's</b> dependencies container.

+ The information Hilt has about how to provide instances of different types are called <b>bindings</b>.

+ To tell Hilt how to bind instance of the class object add the <b>@Inject annotation to the constructor</b> of that class.

### Scoping an instance to a container 
+ Scoping an instance to a container means that Hilt will provide <b>always the same instance</b> of class object, whenever it's called.

+ As Hilt can produce different containers that have different lifecycles, there are different annotations that scope to those containers.

+ The annotation that scopes an instance to the application container is <b>@Singleton</b>. This annotation will make the application container always provide the same instance regardless of whether the type is used as a dependency of another type or if it needs to be field injected. 

+ Bindings available in containers <b>higher up in the hierarchy, are also available in lower </b>levels of the hierarchy. 

+ The same logic can be applied to all containers attached to Android classes. For example, if you want an activity container to always provide the same instance of a type, you can annotate that type with @ActivityScoped.

### Modules
+ Modules are used to add bindings to Hilt to tell Hilt how to provide instances of different types.

+ Modules include <b>bindings for types that cannot be constructor injected</b> such as interfaces or classes that are not contained in your project (e.g. OkHttpClient - you need to use its builder to create an instance) 

+ A Hilt module is a class annotated with <b>@Module and @InstallIn</b>. 

+ <b>@Module</b> tells Hilt this is a module.

+ <b>@InstallIn</b> tells Hilt in which containers the bindings are available:``` @InstallIn(ApplicationComponent::class)```

+ Functions inside the modules are annotaded with </b>@Binds or @Provides.</b>

+ The return type of the <b>@Provides-annotated</b> function tells Hilt the binding's type or how to provide instances of that type. The function parameters are the dependencies of the type. E.g:
```
    @Provides
        fun provideLogDao(database: AppDatabase): LogDao {
            return database.logDao()
      }
```
+ To tell Hilt what implementation to use for an interface, use the <b>@Binds</b> annotation on a function inside a Hilt module. @Binds must annotate an abstract function. The return type of the abstract function is the interface we want to provide an implementation.

+ Hilt Modules <b>cannot contain both non-static and abstract binding methods</b>, so you cannot place @Binds and @Provides annotations in the same class.

+ In Kotlin, modules that only contain @Provides functions can be <b>object</b> classes. In this way, providers get optimized and almost in-lined in generated code.

+ If @Binds or @Provides are used as a binding for a type, <b>the scoping annotations in the type are not required.</b>

+ For better organization, a module's <b>name</b> should convey the type of information it provides

+ Each Hilt container comes with a set of <b>default bindings </b> that can be injected as dependencies into  custom bindings. For example, as you might need the Context class from either the application or the activity, Hilt provides the @ApplicationContext and @ActivityContext qualifiers.


### Qualifiers
+ A qualifier is an annotation used to <b>identify a binding</b>. It's used in the situation when we have e.g. <b>two implementations for the same interface</b>.

+ If there is more than one implementation of the same interface we define binding for each of them in the Modules. 

+ To avoid <b>DuplicateBindings</b> error we have  explicityly tell Hitl which implementation to bing with a qualifier annotation.

+ We need to define separate qualifier for each bingding module. Then we have to add Qualifier annotation in <b>2 places:</b> in the class next to the @Inject annotation for the injected field and above the module function.


### Components
+ For each Android class that can be injected by Hilt, there's an associated Hilt Component:
ApplicationComponent	     -> injector for Application
ActivityRetainedComponent -> injector for ViewModel
ActivityComponent         -> injector for Activity
FragmentComponent         -> injector for Fragment
ViewComponent		          -> injector for View
ViewWithFragmentComponent -> injector for  View annotated with @WithFragmentBindings
ServiceComponent 	        -> injector for Service

+ Bindings available in containers higher up in the hierarchy, are also available in lower levels of the hierarchy. <b>E.g. if instance is provided in the Activity container it's also available in a Fragment container and a View container.</b>

+ Hilt automatically creates and destroys instances of generated component classes following the lifecycle of the corresponding Android classes.

### @EntryPoint
+ Hilt comes with support for the most common Android components. However, you might need to perform field injection in classes that either are not supported directly by Hilt or cannot use Hilt. In those cases, you can use @EntryPoint.

+ An entry point is an interface with an <b>accessor method for each binding type we want</b> (including its qualifier). Also, the interface must be annotated with @InstallIn to specify the component in which to install the entry point. The best practice is adding the new entry point interface inside the class that uses it.

+ Interface is annotated with the @EntryPoint and it's installed in the required component. Inside the interface we expose methods for the bindings we want to access.

### Use hilt:
+ Add Gradle plugin (project build.gradle)
 
 ```
buildscript {
    ...
    ext.hilt_version = '2.28-alpha'
    dependencies {
        ...
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
    }
}
 ```

+ Apply the plugin (app/build.gradle)
  ```apply plugin: 'dagger.hilt.android.plugin```

+ Library dependencies:
```
  implementation "com.google.dagger:hilt-android:$hilt_version"
  kapt "com.google.dagger:hilt-android-compiler:$hilt_version" 
  ```
    
### Testing

+ Testing with Hilt requires no maintenance because Hilt automatically generates a new set of components for each test.

+ Instrumented tests using Hilt need to be executed in an Application that supports Hilt. The library already comes with HiltTestApplication that we can use to run our UI tests. Specifying the Application to use in tests is done by creating a new test runner in the project: create <b>CustomTestRunner</b>  extending AndroidJUnitRunner():
In the app/build.gradle file: replace the default testInstrumentationRunner content with: ```"com.example.android.hilt.CustomTestRunner"```

+ For an emulator test class to use Hilt, it needs to be annotated with <b> @HiltAndroidTest</b> , which is responsible for generating the Hilt components for each test

+ Use the <b>HiltAndroidRule</b> that manages the components' state and is used to perform injection on your test (HiltAndroidRule(this)).

+ Dependencies:
```
   // Hilt testing dependency
   androidTestImplementation "com.google.dagger:hilt-android-testing:$hilt_version"
   // Make Hilt generate code in the androidTest folder
   kaptAndroidTest "com.google.dagger:hilt-android-compiler:$hilt_version"
   ```

### Licence
Copyright (C) 2020 The Android Open Source Project (all resources are from Codelabs Google Developers).
