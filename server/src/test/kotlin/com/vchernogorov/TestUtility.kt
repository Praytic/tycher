package com.vchernogorov

import org.mockito.Mockito

private fun <T> anyObject(): T {
  return Mockito.anyObject<T>()
}
