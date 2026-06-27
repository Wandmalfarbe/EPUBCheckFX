<img src="icon.png" align="right" height="110"/>

# EPUBCheckFX

A graphical interface for the official [W3C EPUBCheck](https://www.w3.org/publishing/epubcheck/) conformance checker.

## Screenshots

![EPUBCheckFX Screenshot Light Mode](img/EPUBCheckFX-Screenshot-Light.png)

![EPUBCheckFX Screenshot Dark Mode](img/EPUBCheckFX-Screenshot-Dark.png)

## Installation / Usage

> [!IMPORTANT]  
> An installation of [Java 21](https://adoptium.net/) (JRE) or later is required to run the application.

Download the correct version of the application for your operating system on the [releases page](https://github.com/Wandmalfarbe/EPUBCheckFX/releases).

### Windows

1. Extract the downloaded ZIP file.
2. Double-click the extracted executable `EPUBCheckFX.exe`.

### macOS

> [!IMPORTANT]  
> ARM based Macs are currently unsupported. The macOS application or the JAR file will only run on intel based Macs.

1. Double-click the downloaded disk image (DMG file).
2. Drag the application `EPUBCheckFX` into the `Application` folder.
3. Start the application `EPUBCheckFX` from the `Application` folder by double-clicking.

### Linux

1. Extract the downloaded tar.gz archive by executing `tar -zxvf EPUBCheckFX-x.x.x-linux.tar.gz` in the terminal (where `x.x.x` is the current version number).
2. Start the application by double-clicking on the file `EPUBCheckFX-x.x.x.jar`.

    One can also start the application from the command line by executing the following command:

    ``` shell
    java -jar EPUBCheckFX-x.x.x.jar
    ```

## Version Compatibility

The table below shows the Java version required to run the program (JRE) and the EPUBCheck version used by the current release and previous releases.

| EPUBCheckFX   | minimum JRE | included EPUBCheck |
|---------------|-------------|--------------------|
| 1.6.0         | 21          | [5.3.0]            |
| 1.5.0         | 21          | [5.2.1]            |
| 1.0.0 – 1.4.0 | 17          | [5.1.0]            |

## Documentation

Please refer to the EPUBCheck documentation at https://www.w3.org/publishing/epubcheck/ or the EPUBCheck GitHub Wiki at https://github.com/w3c/epubcheck/wiki.

## Build / Development

Install the required dependencies:

* [Java 21](https://adoptium.net/)
* [Apache Maven](https://maven.apache.org/)

In order to build the runnable JAR file and all other artifacts, you have to run the following Maven command in the terminal:

```
mvn clean package
```

The macOS build is only possible on Macs and disabled during the normal build. You have to pass the profile `macos` to
Maven in order to build the `dmg` file:

```
mvn clean package -Pmacos
```

## Known Issues / Bugs

- The App is only translated in English and German.
- It is not possible to select an EPUB folder with the button on the start page. One can however drag an EPUB folder on the window to validate it.

## Credits

* EPUBCheckFX uses [EPUBCheck](https://github.com/w3c/epubcheck), the official W3C conformance checker for EPUB publications, to perform the EPUB validation.
* EPUBCheckFX is an homage to the [pagina EPUB-Checker](https://github.com/paginagmbh/EPUB-Checker) however it is a completely independent project and does not share any code.

## License

This project is open source licensed under the BSD 3-Clause License. Please see the [LICENSE file](LICENSE) for more information.

[5.3.0]: https://github.com/w3c/epubcheck/releases/tag/v5.3.0
[5.2.1]: https://github.com/w3c/epubcheck/releases/tag/v5.2.1
[5.1.0]: https://github.com/w3c/epubcheck/releases/tag/v5.1.0
