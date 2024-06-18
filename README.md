Android Start Template
==================

To start a new project:

- Update project name in ``settings.gradle``
- Update package structure at folder ``app/src/main/java``
- Then update package name at ``app/build.gradle``

Structure:

- core module - pure kotlin module to store only your business logics and interfaces that your app will implement
- android module - android library to store any interaction with the framework. core module will have all the interfaces and android module will implement any of those that needs interaction with the framework
- app module - where you will bring together the core and android modules to make the application works. This module will not interact with the framework, only with libs and the core/android module.