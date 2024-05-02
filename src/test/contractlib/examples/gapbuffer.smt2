(declare-sort Char 0)

(declare-datatypes
  ((GapBuffer 0))
   (((GapBuffer (position Int)
                (content (Seq Char))))))

(declare-proc GapBuffer.init
    ()
    ())