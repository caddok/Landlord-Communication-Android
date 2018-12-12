package source.kevtimov.landlordcommunicationapp.async

import javax.inject.Inject

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import source.kevtimov.landlordcommunicationapp.async.base.SchedulerProvider

class AsyncSchedulerProvider @Inject
constructor() : SchedulerProvider {

    override fun background(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    companion object {
        private var instance: SchedulerProvider? = null

        val schedulerProvider: SchedulerProvider
            get() {
                if (instance == null) {
                    instance = AsyncSchedulerProvider()
                }
                return instance as SchedulerProvider
            }
    }
}
