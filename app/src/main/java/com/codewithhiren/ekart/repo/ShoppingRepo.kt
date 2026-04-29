package com.codewithhiren.ekart.repo

import android.net.Uri
import com.codewithhiren.ekart.di.DispatcherIO
import com.codewithhiren.ekart.di.StorageProfilePic
import com.codewithhiren.ekart.model.Address
import com.codewithhiren.ekart.model.CartProduct
import com.codewithhiren.ekart.model.Order
import com.codewithhiren.ekart.model.Product
import com.codewithhiren.ekart.model.User
import com.codewithhiren.ekart.utils.CartProductEnum
import com.codewithhiren.ekart.utils.FireStoreCollection
import com.codewithhiren.ekart.utils.NetworkResponse
import com.codewithhiren.ekart.utils.UserSubCollection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface ShoppingRepo {
    fun getParticularCategoryProducts(category: String): Flow<NetworkResponse<List<Product>>>
    fun getAllProducts(): Flow<NetworkResponse<List<Product>>>
    fun addProductToCart(cartProduct: CartProduct): Flow<NetworkResponse<String>>
    fun getCartProducts(): Flow<NetworkResponse<List<CartProduct>>>
    fun changeQuantityOfCartProduct(cartProduct: CartProduct): Flow<NetworkResponse<String>>
    fun getUserAddresses(): Flow<NetworkResponse<List<Address>>>
    fun addUserAddress(address: Address): Flow<NetworkResponse<String>>
    fun placeOrder(order: Order): Flow<NetworkResponse<String>>
    fun getRegisteredUserProfile(): Flow<NetworkResponse<User>>
    fun changeUserProfile(user: User, userPic: Uri?): Flow<NetworkResponse<String>>
    fun getUserOrders(): Flow<NetworkResponse<List<Order>>>
}

class ShoppingRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @StorageProfilePic private val storageReference: StorageReference,
    private val authRepo: AuthRepo,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) : ShoppingRepo {

    override fun getParticularCategoryProducts(category: String): Flow<NetworkResponse<List<Product>>> =
        flow {
            emit(NetworkResponse.Loading())
            try {
                val querySnapshot = fireStore.collection(FireStoreCollection.Products.name)
                    .whereArrayContains("category", category)
                    .get()
                    .await()

                val productList = mutableListOf<Product>()
                querySnapshot.documents.forEach {
                    productList.add(it.toObject(Product::class.java)!!)
                }
                emit(NetworkResponse.Success(productList.toList()))

            } catch (e: Exception) {
                emit(NetworkResponse.Error(e.localizedMessage ?: "Something went wrong"))
            }

        }.flowOn(dispatcherIO)

    override fun getAllProducts(): Flow<NetworkResponse<List<Product>>> =
        flow {
            emit(NetworkResponse.Loading())
            try {
                val querySnapshot =
                    fireStore.collection(FireStoreCollection.Products.name).get().await()
                val productList = mutableListOf<Product>()
                querySnapshot.documents.forEach {
                    productList.add(it.toObject(Product::class.java)!!)
                }
                emit(NetworkResponse.Success(productList.toList()))
            } catch (e: Exception) {
                emit(NetworkResponse.Error(e.localizedMessage ?: "Something went wrong"))
            }

        }.flowOn(dispatcherIO)

    override fun addProductToCart(cartProduct: CartProduct): Flow<NetworkResponse<String>> =
        flow {
            emit(NetworkResponse.Loading())
            try {
                val collectionRef =
                    fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                        .collection(UserSubCollection.Cart.name)

                val querySnapshot = collectionRef
                    .whereEqualTo("product.id", cartProduct.product.id)
                    .whereEqualTo(CartProductEnum.selectedColor.name, cartProduct.selectedColor)
                    .whereEqualTo(CartProductEnum.selectedSize.name, cartProduct.selectedSize)
                    .get()
                    .await()

                if (querySnapshot.isEmpty) {
                    collectionRef.document().set(cartProduct).await()
                    emit(NetworkResponse.Success("Added to cart"))
                } else if (querySnapshot.documents.size == 1)
                    emit(NetworkResponse.Error("This item is already added"))
                else
                    emit(NetworkResponse.Error("Something went wrong!!"))

            } catch (e: Exception) {
                emit(NetworkResponse.Error(e.localizedMessage ?: "Something went wrong!!"))
            }

        }.flowOn(dispatcherIO)

    override fun getCartProducts(): Flow<NetworkResponse<List<CartProduct>>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            val listener = fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                .collection(UserSubCollection.Cart.name)
                .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) {
                        trySend(
                            NetworkResponse.Error(
                                error.localizedMessage ?: "Something went wrong!!"
                            )
                        )
                        return@addSnapshotListener
                    } else {
                        if (value != null) {
                            val cartProductsList = mutableListOf<CartProduct>()
                            value.documents.forEach {
                                cartProductsList.add(it.toObject(CartProduct::class.java)!!)
                            }
                            trySend(NetworkResponse.Success(cartProductsList.toList()))
                        } else
                            trySend(NetworkResponse.Error("Something went wrong!!"))
                    }
                }
            awaitClose {
                listener.remove()
            }
        }.flowOn(dispatcherIO)

    override fun changeQuantityOfCartProduct(cartProduct: CartProduct): Flow<NetworkResponse<String>> =
        flow {
            emit(NetworkResponse.Loading())
            try {
                val querySnapshot =
                    fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                        .collection(UserSubCollection.Cart.name)
                        .whereEqualTo("product.id", cartProduct.product.id)
                        .whereEqualTo(CartProductEnum.selectedColor.name, cartProduct.selectedColor)
                        .whereEqualTo(CartProductEnum.selectedSize.name, cartProduct.selectedSize)
                        .get()
                        .await()
                if (querySnapshot.isEmpty)
                    emit(NetworkResponse.Error("This product is not in your cart"))
                else if (querySnapshot.documents.size == 1) {
                    try {
                        querySnapshot.documents[0].reference
                            .update(CartProductEnum.quantity.name, cartProduct.quantity)
                            .await()
                        emit(NetworkResponse.Success(""))
                    } catch (_: Exception) {
                        emit(NetworkResponse.Error("Quantity is not changed"))
                    }
                } else
                    emit(NetworkResponse.Error("Something went wrong!!"))
            } catch (e: Exception) {
                emit(NetworkResponse.Error(e.localizedMessage ?: "Something went wrong!!"))
            }

        }.flowOn(dispatcherIO)

    override fun getUserAddresses(): Flow<NetworkResponse<List<Address>>> =
        flow {
            emit(NetworkResponse.Loading())
            try {
                val task = fireStore
                    .collection(FireStoreCollection.User.name)
                    .document(auth.uid!!)
                    .collection(UserSubCollection.Address.name)
                    .get()
                    .await()
                val addressList = mutableListOf<Address>()
                task.documents.forEach {
                    addressList.add(it.toObject(Address::class.java)!!)
                }
                emit(NetworkResponse.Success(addressList.toList()))

            } catch (e: Exception) {
                emit(NetworkResponse.Error(e.localizedMessage ?: "Something went wrong!!"))
            }
        }.flowOn(dispatcherIO)

    override fun addUserAddress(address: Address): Flow<NetworkResponse<String>> =
        flow {
            emit(NetworkResponse.Loading())
            try {
                fireStore
                    .collection(FireStoreCollection.User.name)
                    .document(auth.uid!!)
                    .collection(UserSubCollection.Address.name)
                    .document()
                    .set(address)
                    .await()
                emit(NetworkResponse.Success("Address successfully added"))
            } catch (e: Exception) {
                emit(NetworkResponse.Error(e.localizedMessage ?: "Address is not added"))
            }

        }.flowOn(dispatcherIO)

    override fun placeOrder(order: Order): Flow<NetworkResponse<String>> =
        flow {
            emit(NetworkResponse.Loading())
            try {
                val uid = auth.uid ?: throw Exception("User not authenticated")
                val task = fireStore
                    .collection(FireStoreCollection.User.name)
                    .document(auth.uid!!)
                    .collection(UserSubCollection.Cart.name)
                    .get()
                    .await()

                fireStore.runBatch { batch ->
                    val orderRef = fireStore
                        .collection(FireStoreCollection.User.name)
                        .document(uid)
                        .collection(UserSubCollection.Orders.name)
                        .document()

                    batch.set(orderRef, order)

                    task.documents.forEach { documentSnapshot ->
                        batch.delete(documentSnapshot.reference)
                    }

                    val userHavingOrder = fireStore
                        .collection(FireStoreCollection.UserHavingOrder.name)
                        .document(uid)

                    val emailMap = mapOf("email" to auth.currentUser!!.email!!)
                    batch.set(userHavingOrder, emailMap)
                }.await()
                emit(NetworkResponse.Success("Order successfully placed"))
            } catch (e: Exception) {
                emit(NetworkResponse.Error(e.localizedMessage ?: "Order is not placed"))
            }

        }.flowOn(dispatcherIO)

    override fun getRegisteredUserProfile(): Flow<NetworkResponse<User>> =
        flow {
            emit(NetworkResponse.Loading())
            try {
                val uid = auth.uid ?: throw Exception("User not authenticated")

                val registerUser2ref = fireStore
                    .collection(FireStoreCollection.RegisteredUsers2.name)
                    .document(uid)
                    .get()
                    .await()

                emit(NetworkResponse.Success(registerUser2ref.toObject(User::class.java)!!))
            } catch (e: Exception) {
                emit(NetworkResponse.Error(e.localizedMessage ?: "Something went wrong!!"))
            }

        }.flowOn(dispatcherIO)

    override fun changeUserProfile(user: User, userPic: Uri?): Flow<NetworkResponse<String>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())

            if (userPic == null) {
                trySend(changeProfile(user))
            } else {
                storageReference.putFile(userPic)
                    .addOnSuccessListener {
                        storageReference.downloadUrl
                            .addOnSuccessListener {
                                launch(dispatcherIO) {
                                    trySend(changeProfile(user.copy(imagePath = it.toString())))
                                }
                            }
                            .addOnFailureListener {
                                trySend(NetworkResponse.Error("Your profile is not changed"))
                            }
                    }
                    .addOnFailureListener {
                        trySend(NetworkResponse.Error("Your profile is not changed"))
                    }
            }
            awaitClose()
        }
    /*fun changeUserProfile2(user: User, userPic: Uri?): Flow<NetworkResponse<String>> =
        flow {
            emit(NetworkResponse.Loading())

            if (userPic == null) {
                emit(changeProfile(user))
            } else {
                try {
                    storageReference.putFile(userPic).await()
                    storageReference.downloadUrl.await()
                    emit(changeProfile(user.copy(imagePath = userPic.toString())))

                } catch (_: Exception) {
                    emit(NetworkResponse.Error("Your profile is not changed"))
                }
            }
        }*/

    private suspend fun changeProfile(user: User): NetworkResponse<String> {
        return if (authRepo.saveRegisteredUsers(user))
            NetworkResponse.Success("Your profile is changed")
        else
            NetworkResponse.Error("Your profile is not changed")
    }

    override fun getUserOrders(): Flow<NetworkResponse<List<Order>>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                .collection(UserSubCollection.Orders.name).get()
                .addOnSuccessListener {
                    val userOrders = mutableListOf<Order>()
                    it.documents.forEach { documentSnapshot ->
                        userOrders.add(documentSnapshot.toObject(Order::class.java)!!)
                    }
                    trySend(NetworkResponse.Success(userOrders))
                }
                .addOnFailureListener {
                    trySend(NetworkResponse.Error(it.localizedMessage ?: "Something went wrong!!"))
                }
            awaitClose()
        }
}

/*
class ShoppingRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @StorageProfilePic private val storageReference: StorageReference,
    private val authRepo: AuthRepo,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) : ShoppingRepo {

    override fun getParticularCategoryProducts(category: String): Flow<NetworkResponse<List<Product>>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            fireStore.collection(FireStoreCollection.Products.name)
                .whereArrayContains("category", category)
                .get()
                .addOnSuccessListener { it ->
                    val productList = mutableListOf<Product>()
                    it.documents.forEach {
                        productList.add(it.toObject(Product::class.java)!!)
                    }
                    trySend(NetworkResponse.Success(productList))
                }
                .addOnFailureListener { it: Exception ->
                    trySend(NetworkResponse.Error(it.localizedMessage ?: "Something went wrong"))
                }
            awaitClose()
        }

    override fun getAllProducts(): Flow<NetworkResponse<List<Product>>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            fireStore.collection(FireStoreCollection.Products.name).get()
                .addOnSuccessListener { it ->
                    val productList = mutableListOf<Product>()
                    it.documents.forEach {
                        productList.add(it.toObject(Product::class.java)!!)
                    }
                    trySend(NetworkResponse.Success(productList))
                }
                .addOnFailureListener { it: Exception ->
                    trySend(NetworkResponse.Error(it.localizedMessage ?: "Something went wrong"))
                }
            awaitClose()
        }

    override fun addProductToCart(cartProduct: CartProduct): Flow<NetworkResponse<String>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            val collectionRef =
                fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                    .collection(UserSubCollection.Cart.name)

            collectionRef
                .whereEqualTo("product.id", cartProduct.product.id)
                .whereEqualTo(CartProductEnum.selectedColor.name, cartProduct.selectedColor)
                .whereEqualTo(CartProductEnum.selectedSize.name, cartProduct.selectedSize)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        collectionRef.document().set(cartProduct)
                            .addOnSuccessListener {
                                trySend(NetworkResponse.Success("Added to cart"))
                            }
                            .addOnFailureListener {
                                trySend(
                                    NetworkResponse.Error(
                                        it.localizedMessage ?: "Something went wrong!!"
                                    )
                                )
                            }
                    } else if (it.documents.size == 1)
                        trySend(NetworkResponse.Error("This item is already added"))
                    else
                        trySend(NetworkResponse.Error("Something went wrong!!"))
                }
                .addOnFailureListener {
                    trySend(NetworkResponse.Error(it.localizedMessage ?: "Something went wrong!!"))
                }
            awaitClose()
        }

    override fun getCartProducts(): Flow<NetworkResponse<List<CartProduct>>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            val listener = fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                .collection(UserSubCollection.Cart.name)
                .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) {
                        trySend(NetworkResponse.Error(error.localizedMessage ?: "Something went wrong!!"))
                        return@addSnapshotListener
                    } else {
                        if (value != null) {
                            val cartProductsList = mutableListOf<CartProduct>()
                            value.documents.forEach {
                                cartProductsList.add(it.toObject(CartProduct::class.java)!!)
                            }
                            trySend(NetworkResponse.Success(cartProductsList.toList()))
                        } else
                            trySend(NetworkResponse.Error("Something went wrong!!"))
                    }
                }
            awaitClose {
                listener.remove()
            }
        }.flowOn(dispatcherIO)

    override fun changeQuantityOfCartProduct(cartProduct: CartProduct): Flow<NetworkResponse<String>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                .collection(UserSubCollection.Cart.name)
                .whereEqualTo("product.id", cartProduct.product.id)
                .whereEqualTo(CartProductEnum.selectedColor.name, cartProduct.selectedColor)
                .whereEqualTo(CartProductEnum.selectedSize.name, cartProduct.selectedSize)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty)
                        trySend(NetworkResponse.Error("This product is not in your cart"))
                    else if (it.documents.size == 1) {
                        it.documents[0].reference
                            .update(CartProductEnum.quantity.name, cartProduct.quantity)
                            .addOnSuccessListener {
                                trySend(NetworkResponse.Success(""))
                            }
                            .addOnFailureListener {
                                trySend(NetworkResponse.Error("Quantity is not changed"))
                            }
                    } else
                        trySend(NetworkResponse.Error("Something went wrong!!"))
                }
                .addOnFailureListener {
                    trySend(NetworkResponse.Error(it.localizedMessage ?: "Something went wrong!!"))
                }
            awaitClose()
        }

    override fun getUserAddresses(): Flow<NetworkResponse<List<Address>>> =
        flow {
            emit(NetworkResponse.Loading())
            try {
                val task = fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                    .collection(UserSubCollection.Address.name).get().await()
                val addressList = mutableListOf<Address>()
                task.documents.forEach {
                    addressList.add(it.toObject(Address::class.java)!!)
                }
                emit(NetworkResponse.Success(addressList.toList()))

            } catch (e: Exception) {
                emit(NetworkResponse.Error(e.localizedMessage ?: "Something went wrong!!"))
            }
        }.flowOn(dispatcherIO)

    override fun addUserAddress(address: Address): Flow<NetworkResponse<String>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                .collection(UserSubCollection.Address.name).document().set(address)
                .addOnSuccessListener {
                    trySend(NetworkResponse.Success("Address successfully added"))
                }
                .addOnFailureListener {
                    trySend(NetworkResponse.Error(it.localizedMessage ?: "Address is not added"))
                }
            awaitClose()
        }

    override fun placeOrder(order: Order): Flow<NetworkResponse<String>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            fireStore.runBatch {
                fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                    .collection(UserSubCollection.Orders.name).document().set(order)
                fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                    .collection(UserSubCollection.Cart.name).get()
                    .addOnSuccessListener {
                        it.documents.forEach { documentSnapshot ->
                            documentSnapshot.reference.delete()
                        }
                    }
                val emailMap = mapOf("email" to auth.currentUser!!.email!!)
                fireStore.collection(FireStoreCollection.UserHavingOrder.name).document(auth.uid!!)
                    .set(emailMap)
            }
                .addOnSuccessListener {
                    trySend(NetworkResponse.Success("Order successfully placed"))
                }
                .addOnFailureListener {
                    trySend(NetworkResponse.Error(it.localizedMessage ?: "Order is not placed"))
                }
            awaitClose()
        }

    override fun getRegisteredUserProfile(): Flow<NetworkResponse<User>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            fireStore.collection(FireStoreCollection.RegisteredUsers2.name)
                .document(auth.uid!!).get()
                .addOnSuccessListener {
                    trySend(NetworkResponse.Success(it.toObject(User::class.java)!!))
                }
                .addOnFailureListener {
                    trySend(NetworkResponse.Error(it.localizedMessage ?: "Something went wrong!!"))
                }
            awaitClose()
        }

    override fun changeUserProfile(user: User, userPic: Uri?): Flow<NetworkResponse<String>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())

            if (userPic == null) {
                trySend(changeProfile(user))
            } else {
                storageReference.putFile(userPic)
                    .addOnSuccessListener {
                        storageReference.downloadUrl
                            .addOnSuccessListener {
                                launch(dispatcherIO) {
                                    trySend(changeProfile(user.copy(imagePath = it.toString())))
                                }
                            }
                            .addOnFailureListener {
                                trySend(NetworkResponse.Error("Your profile is not changed"))
                            }
                    }
                    .addOnFailureListener {
                        trySend(NetworkResponse.Error("Your profile is not changed"))
                    }
            }
            awaitClose()
        }

    private suspend fun changeProfile(user: User): NetworkResponse<String> {
        return if (authRepo.saveRegisteredUsers(user))
            NetworkResponse.Success("Your profile is changed")
        else
            NetworkResponse.Error("Your profile is not changed")
    }

    override fun getUserOrders(): Flow<NetworkResponse<List<Order>>> =
        callbackFlow {
            trySend(NetworkResponse.Loading())
            fireStore.collection(FireStoreCollection.User.name).document(auth.uid!!)
                .collection(UserSubCollection.Orders.name).get()
                .addOnSuccessListener {
                    val userOrders = mutableListOf<Order>()
                    it.documents.forEach {
                        userOrders.add(it.toObject(Order::class.java)!!)
                    }
                    trySend(NetworkResponse.Success(userOrders))
                }
                .addOnFailureListener {
                    trySend(NetworkResponse.Error(it.localizedMessage ?: "Something went wrong!!"))
                }
            awaitClose()
        }
}*/
