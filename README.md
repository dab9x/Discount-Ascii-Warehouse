# Discount-Ascii-Warehouse

A mobile application for exploring the Discount Ascii Warehouse ecommerce platform.

## Architecture Components

- Server NodeJs
- MVVM Architecture
- Language Kotlin on Android platform
- Using Kotlin Coroutines, Hilt DI, Navigation, Retrofit, Lifecycle(ViewModel, LiveData)

## Setup

### 1. Server

Clone project [Discount-Ascii-Warehouse](https://github.com/dab9x/Discount-Ascii-Warehouse.git)

```bash
git clone https://github.com/dab9x/Discount-Ascii-Warehouse.git
```

Open terminal move folder `daw-purchases-master` by `/Discount-Ascii-Warehouse/daw-purchases-master/` 

Enter command 

```bash
- `npm install`
- `npm start`
```

The screen should display `ready on http://0.0.0.0:8000`. Yes server is running.

<p align="center">
    <img src="https://imgur.com/g4XoZ78.png" alt="drawing" />
</p>


### 2. Client (Android Project)

Import project [Discount-Ascii-Warehouse](https://github.com/dab9x/Discount-Ascii-Warehouse.git) in [Android Studio](https://developer.android.com/)

Run project on Emulator 

Config Proxy for Emulator to API working

- `Hostname: 10.0.2.2`
- `Port: 8000`

<p align="center">
    <img src="https://imgur.com/TsWdWBp.png" alt="drawing" />
</p>

Run app 

<p align="center">
    <img src="https://imgur.com/pL2qC88.png" alt="drawing"   width="170"/>
    <img src="https://imgur.com/rx8g0vr.png" alt="drawing"   width="170"/>
    <img src="https://imgur.com/aNc39Pd.png" alt="drawing"   width="170"/>
    <img src="https://imgur.com/fRWQFD3.png" alt="drawing"   width="170"/>
</p>