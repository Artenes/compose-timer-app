package dev.artenes.timer.core.models

//put all of your business rules exceptions here
class MissingPermissionToDisplayNotificationException(val permission: String): Exception()
class InvalidChannelImportanceException(val importance: Int): Exception()