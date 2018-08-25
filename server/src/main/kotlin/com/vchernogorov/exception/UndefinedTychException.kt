package com.vchernogorov.exception

class UndefinedEntityException(entity: Any) : Exception("$entity is not presented in the global map.")