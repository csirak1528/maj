package com.maj.typecheck.handlers

import com.maj.ast._
import com.maj.typecheck.TypeChecker

class ScalarTypeCheckHandler(val typeChecker: TypeChecker) {

  def visit(node: MajInt): MajIntType = new MajIntType()

  def visit(node: MajBool): TypeNode = new MajBoolType()

  def visit(node: MajNull): TypeNode = new MajVoidType()
}
