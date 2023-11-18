# Changelog

All notable changes to this project are documented in this file. On the [releases page](https://github.com/Wandmalfarbe/EPUBCheckFX/releases) you can see all released versions of EPUBCheckFX and download the [latest version](https://github.com/Wandmalfarbe/EPUBCheckFX/releases/latest).

## Unreleased

- Renamed the tabs *Metadata* and *General Information* to *General Metadata* and *File Metadata*.
- Fixed the section naming in the *Metadata* tab.
- Updated dependencies

## [1.2.0] - 2023-10-05

- Add the following export formats:
  - AsciiDoc
  - reStructuredText
  - Textile
  - CSV (Comma-separated values)
  - TSV (Tab-separated values)
- Remove the export format *Markdown (Table)*.
- Remove the additional extracted features from all export formats.
- The ComboBox for the export formats now displays the file extensions of all the formats.
- Don't show empty line numbers when exporting to *plaintext* and *pagina EPUB-Checker Text*.
- Don't HTML encode anything when exporting to *plaintext* and *pagina EPUB-Checker Text*.
- Reports are fully translated.
- Add a menubar where it is possible to change the theme, open links to the project website, or open the about screen.
- Add an About screen that displays copyright information, license information and third-party licenses.
- Add persistence to the `ComboBoxes` for the EPUB profile, the severity filter and the export format.
  They will remember the last selected value even when the application is closed.
- Add the view modes *table* and *list* for the validation results. The view mode will be remembered even
  when the application is closed.

## [1.1.0] - 2023-09-04

- Add a toolbar with a new button to open folders.
- Add a panel below the result table to show the (filtered) severity summary.
- Update the icons for the severities, so they don't all look alike.
- Add icons to the severity filter (CombBox).

## [1.0.0] - 2023-08-12

Initial release with different executables for Windows, macOS and Linux.

[1.2.0]: https://github.com/Wandmalfarbe/EPUBCheckFX/compare/v1.1.0...1.2.0
[1.1.0]: https://github.com/Wandmalfarbe/EPUBCheckFX/compare/v1.0.0...1.1.0
[1.0.0]: https://github.com/Wandmalfarbe/EPUBCheckFX/releases/tag/v1.0.0
