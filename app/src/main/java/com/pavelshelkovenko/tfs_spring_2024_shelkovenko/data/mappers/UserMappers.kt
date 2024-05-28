package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.mappers

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.AccountSettingsDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.UserDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.UserDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.dto.UserOnlineStatusDto
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.AccountSettings
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.UserOnlineStatus

fun UserDto.toUserDomain(
    onlineStatus: UserOnlineStatus = UserOnlineStatus.OFFLINE
): User = User(
    id = this.id,
    avatarUrl = this.avatarUrl,
    name = this.userName,
    email = this.email ?: this.zulipEmail,
    onlineStatus = onlineStatus
)

fun UserDto.toUserDbo(
    onlineStatus: UserOnlineStatus = UserOnlineStatus.OFFLINE
): UserDbo = UserDbo(
    id = this.id,
    avatarUrl = this.avatarUrl,
    name = this.userName,
    email = this.email ?: this.zulipEmail,
    onlineStatus = onlineStatus
)

fun UserDbo.toUserDomain(): User = User(
    id = this.id,
    avatarUrl = this.avatarUrl,
    name = this.name,
    email = this.email,
    onlineStatus = this.onlineStatus
)

fun User.toUserDbo(): UserDbo = UserDbo(
    id = this.id,
    avatarUrl = this.avatarUrl,
    name = this.name,
    email = this.email,
    onlineStatus = this.onlineStatus
)

fun UserOnlineStatusDto.toUserOnlineStatusDomain(): UserOnlineStatus = when (this) {
    UserOnlineStatusDto.ACTIVE -> UserOnlineStatus.ACTIVE
    UserOnlineStatusDto.IDLE -> UserOnlineStatus.IDLE
    UserOnlineStatusDto.OFFLINE -> UserOnlineStatus.OFFLINE
}

fun AccountSettingsDbo.toAccountSetting(): AccountSettings = AccountSettings(
    userId = this.userId,
    userName = this.userName,
    emailVisibility = this.emailVisibility,
    email = this.email,
    isInvisibleMode = this.isInvisibleMode,
    avatarUrl = this.avatarUrl
)

fun AccountSettings.toAccountSettingDbo(): AccountSettingsDbo = AccountSettingsDbo(
    userId = this.userId,
    userName = this.userName,
    emailVisibility = this.emailVisibility,
    email = this.email,
    isInvisibleMode = this.isInvisibleMode,
    avatarUrl = this.avatarUrl
)