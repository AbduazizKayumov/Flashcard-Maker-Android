package com.piapps.flashcardpro.features.main.interactor

import com.piapps.flashcardpro.core.db.DatabaseRepository
import com.piapps.flashcardpro.core.extension.doAsync
import com.piapps.flashcardpro.core.extension.uiThread
import com.piapps.flashcardpro.features.main.entity.SetView
import javax.inject.Inject

/**
 * Created by abduaziz on 2019-09-27 at 23:16.
 */

class GetAllSets
@Inject constructor(private val repository: DatabaseRepository) {
    operator fun invoke(onResult: (ArrayList<SetView>) -> Unit = {}) {
        doAsync {
            val result = repository.getAllSets()
            val list = arrayListOf<SetView>()
            result.forEach {
                list.add(it.toSetView())
            }
            uiThread {
                onResult(list)
            }
        }
    }
}