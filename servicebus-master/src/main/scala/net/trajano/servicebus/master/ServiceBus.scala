package net.trajano.servicebus.master

trait ServiceBus {
  def tell(message: AnyRef): Unit
  def ask(message: AnyRef): AnyRef
}