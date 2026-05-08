## > Active development is currently happening on the `windows` branch.

<div align="center">

<a href="https://github.com/MirahApp/mirah">
    <img src=".github/assets/icon-1024.png" width="80" alt="Mirah Logo" />
</a>

# Mirah

Free and open source manga reader for Windows.

[![CI](https://img.shields.io/github/actions/workflow/status/MirahApp/mirah/build.yml?label=CI&labelColor=27303D)](https://github.com/MirahApp/mirah/actions)
[![License: Apache-2.0](https://img.shields.io/badge/License-Apache--2.0-blue.svg?labelColor=27303D)](/LICENSE)

</div>

## Download

No stable release yet.

## Features

* Local reading of content.
* Library management.
* Desktop extension pipeline (Keiyoushi index fetch, APK install, source loading).
* Browse -> Sources tab integration for installed extension sources.
* Tracker support: MyAnimeList, AniList, Kitsu, MangaUpdates, Shikimori, and Bangumi.
* Categories to organize your library.
* Light and dark themes.
* Backup and restore.

## Building from source

Requirements: JDK 21

```bash
./gradlew packageMsi
```

## Contributing

Please refer to [CONTRIBUTING.md](CONTRIBUTING.md) for details.

## License

```
Copyright © 2015 Javier Tomás
Copyright © 2024 Mihon Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
