package com.codewithhiren.ekart.repo

import com.codewithhiren.ekart.di.DispatcherIO
import com.codewithhiren.ekart.model.User
import com.codewithhiren.ekart.utils.FireStoreCollection
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.NetworkResponse.Loading
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface AuthRepo {
    fun registerWithEmailAndPassword(user: User, password: String): Flow<NetworkResponse<String>>
    fun loginWithEmailAndPassword(email: String, password: String): Flow<NetworkResponse<String>>
    fun resetPasswordUsingEmailLink(email: String): Flow<NetworkResponse<String>>
    suspend fun saveRegisteredUsers(user: User): Boolean
}


class AuthRepoImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) : AuthRepo {


    override fun registerWithEmailAndPassword(
        user: User,
        password: String
    ): Flow<NetworkResponse<String>> =
        flow {
            emit(Loading())
            try {
                auth.createUserWithEmailAndPassword(user.email, password).await()
                if (saveRegisteredUsers(user))
                    emit(NetworkResponse.Success("Successfully registered"))
                else{
                    auth.currentUser!!.delete()
                    emit(NetworkResponse.Error("Something went wrong!!"))
                }
            } catch (e: Exception) {
                val exception = when (e) {
                    is FirebaseAuthUserCollisionException -> "Email is already existed"
                    else -> e.localizedMessage ?: "Something went wrong!!"
                }
                emit(NetworkResponse.Error(exception))
            }

    }.flowOn(dispatcherIO)

    override suspend fun saveRegisteredUsers(user: User): Boolean {
        return  try {
            val uid = auth.currentUser?.uid ?: return false
            fireStore.collection(FireStoreCollection.RegisteredUsers2.name)
                .document(uid)
                .set(user)
                .await()
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<NetworkResponse<String>> =
        flow {
            emit(Loading())
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                emit(NetworkResponse.Success("Successfully log in"))
            } catch (e: Exception) {
                val exception = when (e) {
                    is FirebaseAuthInvalidUserException -> "Invalid email format"
                    is FirebaseAuthInvalidCredentialsException -> "Wrong email or password"
                    is FirebaseTooManyRequestsException -> "Your account is temporarily blocked"
                    else -> e.localizedMessage ?: "Something went wrong!!"
                }
                emit(NetworkResponse.Error(exception))
            }

        }.flowOn(dispatcherIO)

    override fun resetPasswordUsingEmailLink(email: String) : Flow<NetworkResponse<String>> =
        flow {
            emit(Loading())
            try {
                auth.sendPasswordResetEmail(email).await()
                emit(NetworkResponse.Success("Reset link sent to your email"))
            } catch (e: Exception) {
                emit(NetworkResponse.Error(e.localizedMessage ?: "Something went wrong!!"))
            }
        }.flowOn(dispatcherIO)

}

/*class AuthRepoImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) : AuthRepo {

    override fun registerWithEmailAndPassword(
        user: User,
        password: String
    ): Flow<NetworkResponse<String>> =
        callbackFlow {
            trySend(Loading())
            auth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    launch(dispatcherIO) {
                        if (saveRegisteredUsers(user))
                            trySend(NetworkResponse.Success("Successfully registered"))
                        else{
                            auth.currentUser!!.delete()
                            trySend(NetworkResponse.Error("Something went wrong!!"))
                        }
                    }
                }
                .addOnFailureListener {
                    val exception = when (it) {
                        is FirebaseAuthUserCollisionException -> "Email is already existed"
                        else -> it.localizedMessage ?: "Something went wrong!!"
                    }
                    trySend(NetworkResponse.Error(exception))
                }

            awaitClose()
        }

    override suspend fun saveRegisteredUsers(user: User): Boolean {
        return  try {
            val uid = auth.currentUser?.uid ?: return false
            fireStore.collection(FireStoreCollection.RegisteredUsers2.name)
                .document(uid)
                .set(user)
                .await()
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<NetworkResponse<String>> =
        callbackFlow {
            trySend(Loading())
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    trySend(NetworkResponse.Success("Successfully log in"))
                }
                .addOnFailureListener {
                    val exception = when (it) {
                        is FirebaseAuthInvalidUserException -> "Invalid email format"
                        is FirebaseAuthInvalidCredentialsException -> "Wrong email or password"
                        is FirebaseTooManyRequestsException -> "Your account is temporarily blocked"
                        else -> it.localizedMessage ?: "Something went wrong!!"
                    }
                    trySend(NetworkResponse.Error(exception))
                }
            awaitClose()
        }

    override fun resetPasswordUsingEmailLink(email: String): Flow<NetworkResponse<String>> =
        callbackFlow {
            trySend(Loading())
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    trySend(NetworkResponse.Success("Reset link sent to your email"))
                }
                .addOnFailureListener {
                    trySend(NetworkResponse.Error(it.localizedMessage ?: "Something went wrong!!"))
                }
            awaitClose()
        }

}*/
