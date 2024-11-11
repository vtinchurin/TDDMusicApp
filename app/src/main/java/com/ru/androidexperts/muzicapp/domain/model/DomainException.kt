package com.ru.androidexperts.muzicapp.domain.model

interface DomainException : TrackModel {

    class Base(
        private val resId: Int,
    ) : Exception(), DomainException {

        override fun <T : Any> map(mapper: TrackModel.Mapper<T>): T {
            return mapper.mapToError(resId)
        }
    }
}

