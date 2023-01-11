# WeatherApp
Simple Weather app using kotlin. Fetches weather data of current location. MVVM architecture.

Kotlin
MVVM
Coroutine (for asynchronous task)
Flow (for asynchronous data streaming between different layers of app, can use live data)
room database (for storing sql data )
retrofit ( for server communication)
single source of truth pattern (data fetches from server,stores to database,then comes to presentation layer wrapped in flow>
