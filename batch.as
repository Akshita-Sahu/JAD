dashboard -n 1
sysprop
watch jad.Test test "@com.akshita.jad.Test@n.entrySet().iterator.{? #this.key.name()=='STOP' }" -n 2
