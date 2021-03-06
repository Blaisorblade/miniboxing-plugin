//
//     _____   .__         .__ ___.                    .__ scala-miniboxing.org
//    /     \  |__|  ____  |__|\_ |__    ____  ___  ___|__|  ____     ____
//   /  \ /  \ |  | /    \ |  | | __ \  /  _ \ \  \/  /|  | /    \   / ___\
//  /    Y    \|  ||   |  \|  | | \_\ \(  <_> ) >    < |  ||   |  \ / /_/  >
//  \____|__  /|__||___|  /|__| |___  / \____/ /__/\_ \|__||___|  / \___  /
//          \/          \/          \/               \/         \/ /_____/
// Copyright (c) 2011-2015 Scala Team, École polytechnique fédérale de Lausanne
//
// Authors:
//    * Milos Stojanovic
//
package miniboxing.plugin
package transform
package hijack

import scala.tools.nsc.transform.InfoTransform
import scala.collection.immutable.ListMap

trait MiniboxCompileTimeOnlyAddTags extends InfoTransform {
  this: CompileTimeOnlyAddTagsComponent =>

  import global._
  import definitions._
  import scala.reflect.internal.Flags._

  def transformInfo(sym: Symbol, tpe: Type): Type = {

    if (currentRun.compiles(sym) &&
        sym.name == nme.CONSTRUCTOR &&
        (sym.owner.isClass || sym.owner.isTrait) &&
        sym.owner.typeParams.exists(_.hasAnnotation(MinispecClass)))

      // Annotate the constructor with @compileTimeOnly:
      sym.addAnnotation(Annotation(CompileTimeOnlyClass.tpe, List(Literal(Constant("use the miniboxing plugin!"))), ListMap.empty))

    tpe
  }
}
