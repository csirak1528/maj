package com.maj.ir.handlers

import com.maj.ast._
import com.maj.emitters.Emitter
import com.maj.ir._

class FunctionIRGenHandler(val irGenerator: IRGenerator)(implicit emitter: Emitter[IRNode]) {
  def handle(node: Function): Option[IRNode] = {
    val params = node.params.map(irGenerator.addVar)
    emitter.emit(IRFunc(node.name, params))
    irGenerator.visit(node.body)
    emitter.emit(IRReturn(IRNull()))
    None
  }

  def handle(node: Call): Option[IRNode] = {
    val args = node.args.map(irGenerator.visit).map(irGenerator.getResultInAnonVar)
    emitter.emit(IRCall(node.callee, args))
    None
  }

  def handle(node: Return): Option[IRNode] = {
    val term = irGenerator.visit(node.term)
    val result = irGenerator.getResultInAnonOrScalar(term)
    emitter.emit(IRReturn(result))
    None
  }

  def handle(node: Block): Option[IRNode] = {
    node.statements.foreach(irGenerator.visit)
    None
  }

  def handle(node: AsmBlock): Option[IRNode] = {
    emitter.emit(IRAsmBlock(node.statements))
    None
  }

}
