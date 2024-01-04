package com.example.inature3.utility

class UserDataHolder private constructor() {

    var nome: String? = null
    var email: String? = null

    companion object {
        private var instance: UserDataHolder? = null

        fun getInstance(): UserDataHolder {
            if (instance == null) {
                instance = UserDataHolder()
            }
            return instance as UserDataHolder
        }
    }
}