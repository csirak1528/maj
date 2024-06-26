package com.maj.ir.handlers

import com.maj.ast._
import com.maj.emitters.Emitter
import com.maj.ir._

class ControlFlowIRGenHandler(val irGenerator: IRGenerator)(implicit emitter: Emitter[IRNode]) {
  def handle(node: Conditional): Option[IRNode] = {
    val conditionAssign = irGenerator.visit(node.condition)
    val ifFalseLabel = irGenerator.nextLabel

    val anonCondition = irGenerator.getResultInAnonVar(conditionAssign)
    emitter.emit(IRJumpIf(anonCondition, ifFalseLabel))
    irGenerator.visit(node.ifTrue)
    if (node.elseIfTrue.isEmpty) {
      emitter.emit(ifFalseLabel)
    } else {
      val endLabel = irGenerator.nextLabel
      emitter.emit(IRJump(endLabel))
      emitter.emit(ifFalseLabel)
      node.elseIfTrue.map(irGenerator.visit)
      emitter.emit(endLabel)
    }
    None
  }

  def handle(node: Loop): Option[IRNode] = {
    val loopLabel = irGenerator.nextLabel
    val endLabel = irGenerator.nextLabel
    emitter.emit(loopLabel)
    val conditionAssign = irGenerator.visit(node.condition)
    val anonCondition = irGenerator.getResultInAnonVar(conditionAssign)
    emitter.emit(IRJumpIf(anonCondition, endLabel))
    irGenerator.visit(node.body)
    emitter.emit(IRJump(loopLabel))
    emitter.emit(endLabel)
    None
  }
}






