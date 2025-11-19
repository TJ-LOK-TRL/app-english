package com.masterproject.englishapp.permissions

// This is just to be more visual beautiful in the list of AppPermissions

operator fun AppPermission.plus(other: AppPermission): List<AppPermission> =
    listOf(this, other)

operator fun List<AppPermission>.plus(other: AppPermission): List<AppPermission> =
    this + other