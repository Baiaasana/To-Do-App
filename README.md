<h1 align="start">TO-DO app 📑</h1>

<p align="start">  
🗡️  TO-DO app demonstrates modern Android development with Hilt, Flow, Jetpack (Room, ViewModel), and Material Design based on MVVM architecture.
</p>
<br>

## Tech stack & Open-source libraries

<div style="float: right; margin-left: 400px;">
  <img src="/previews/preview.gif" align="right" width="300"/>
</div>

- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
  - [Hilt](https://dagger.dev/hilt/): for dependency injection.
- Architecture
  - MVVM Architecture 
  - Repository Pattern

## Architecture
**TO-DO** app is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).
