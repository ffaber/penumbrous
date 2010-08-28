#################################################################
#
#         .cshrc file
#         Modified By Fred Faber
#         initial setup file for both interactive and noninteractive
#         C-Shells
#
#################################################################
# shell variables read-only: set -r <var>
# set shell variables: no space or some space: set x=y, set x = y
# only interactive commands: if($?prompt) then.... endif
# stty <function> <char> (after stty -a for all vars)
#   ex) stty erase ^h , or stty erase '^?' for DEL
# !# refers to current event
# to add to last command, !! <extra_text>, ex) !! | more
# to prepend to last command, <extra_text> !!
# to refer as text command, !{m}2, ex) more <old_file>2
# to refer to command vars, !!:{0-num}
# to refer to word command, !event:words
#  so !$ is shorthand for !!:!$
# NICE: cp <file_name> !#^.old
# USE !{<letter>}{-<num>} -> !m{-2}:p
#
#################################################################
#
# !event:words:modifiers
# !event:modifiers
#
# p - print resulting command w/o executing
# s/old/new - substitiution ( ^old^new )
# & - repeat previous s substitution
# r - root (everyting but ext - following last dot)
# e - extension
# h - head of pathname (all but last)
# t - tail of pathname
# q - quote words (prevents filename pattern expansion)
# u - make first lowercase letter upper
# l - make first uppercase letter lowercase
# g - apply golbally
# a - apply as many times as posible

# ex) >mv AllocColor.c !#^:s/A/XA
#      mv AllocColor.c XAllocColor.c

#	[root@porthead ~/doc]# dir a b c
#	ls: a: No such file or directory
#	ls: b: No such file or directory
#	ls: c: No such file or directory
#	[root@porthead ~/doc]# !^:p
#	a

#################################################################
# 0	the first (command) word
# n	the nth argument
# ^	the first argument, that is, 1
# $	the last argument
# %	the word matched by an immediately preceding ?s? search
# %	x-y	range of words
# %	-y	abbreviation for 0-y
# %	*	abbreviation for ^-$, or nothing if only 1 word in event
# %	x*	abbreviation for x-$
# %	x-	like x* but omitting word $
# 
# h	removes a trailing path name component, leaving the head.
# r	removes a trailing .xxx component, leaving the root name.
# e	removes all but the extension .xxx part.
# s/l/r/	substitutes l for r
# t	removes all leading path name components, leaving the tail.
# &	repeats the previous substitution.
# g	applies the change once on each word, prefixing the above, 
# 	for example, g&.
# a	applies the change as many times as possible on a single word, 
# 	prefixing the above
# 	(can be used together with g to apply a substitution globally).
# p	prints the new command line but does not execute it.
# q	quotes the substituted words, preventing further substitutions.
# x	is like q, but breaks into words at blanks, tabs and newlines.
#################################################################

#################################################################
# Time - timetool
#################################################################


#################################################################
# BINDKEYS - bindkey -e(emacs) -v(vi) 
#  >echo $version; bindkey (-l)
# ex) in .cshrc: bindkey "^xp" i-search-back (for CTRL-X p)
# ex) to see what's bound: bindkey ^(ctrl-letter)
#                           bindkey [CTRL-V][CTRL-letter]
#      use -b for C-letter notation
#################################################################
bindkey "^f" forward-word
bindkey "^b" backward-word
#bindkey "^ " set-mark-command
#################################################################

#################################################################
# alias
# alias llm 'ls -l \!* | more' (for args of ls, also \!^, \!$
#################################################################

#################################################################
# arguments
# ex) mkdir Proj{1,2,3,4,5}
# ex) diff document{.old,}
#
# ex) more <fil^D> -> to see completion of names
# ex) expansion: more <fil*{^X,*}> -> expand all completions
# CTRL-V (stop from exanding TAB)
#################################################################


#################################################################
#
# precmd ()   a function which is executed just before each prompt
# cwdcmd ()   a function which is executed whenever the directory is changed
# %n          expands to username
# %%m          expands to hostname
# %%~          expands to directory, replacing $HOME with '~'
# %%#          expands to '>' for normal users, '#' for root users
# %%{...%}     includes a string as a literal escape sequence
###############################################################

###############################################################
#       Set the environments
###############################################################

#To make sure they are defined
setenv CVSROOT 
#setenv LD_LIBRARY_PATH ${LD_LIBRARY_PATH}:/usr/local/yk/bin/linux-x86-32
#setenv JAVA_HOME /auto/build/buildtools/java/latest
#setenv JDK_HOME  /usr/java/java-latest
#setenv JAVA_HOME /usr/java/java-latest
#setenv JDK_HOME /home/build/buildtools/java/latest/
#setenv JDK_HOME /usr/local/buildtools/java/jdk1.6.0_01/
setenv JDK_HOME /usr/local/buildtools/java/jdk1.6.0_01_gg1/
setenv JAVA_HOME /usr/local/buildtools/java/jdk1.6.0_01_gg1/
setenv CLASSPATH ${JAVA_HOME}/jre/lib/ext:${JAVA_HOME}/jre/lib
setenv PATH ${JDK_HOME}/bin:${JAVA_HOME}/bin:/bin:/sbin:/usr/bin:/usr/sbin:/usr/X11R6/bin:/usr/local/bin
# setenv PATH /usr/java/java-latest/bin:${PATH}

###########################################################
# ff - 10.05.01 (for invokation from another shell) 
# desc: path for local binaries
###########################################################
#if( `env | grep PATH_SET` == '' ) then
## 1.  System paths
##setenv PATH ${PATH}:/usr/openwin/bin:/usr/ucb:/usr/sbin/:/bin:/sbin
## 2.  Application paths
##setenv PATH ${PATH}:/opt/autosys/bin:$DT_SBACKTRACK_HOME/bin:$SYBASE/bin
## 3.  Para specific paths
#setenv PATH
#${PATH}:/opt/para/tools:/home/paraconf/scripts/bin:/opt/para_prod/tools
## 4.  User System paths
#setenv PATH
#${PATH}:/usr/local/bin:/home/faberf/bin:/opt/gnu/bin:/opt/gnu-1.1/bin
## 5.  Java paths
#setenv PATH
#${PATH}:/opt/para_prod/java/j2se-1.3.1_03/jre/bin/:/opt/jdk-1.2.2/bin
#setenv PATH_SET 1
#endif 

#setenv CLASSPATH ~/lib

#foreach jar ( `ls ~ffaber/lib/*.jar` )
#	setenv CLASSPATH {$CLASSPATH}:$jar
#end

set path=( /usr/local/scripts $path)
set path=( /usr/local/symlinks $path)
set path=( /usr/local/maven/bin $path)
set path=( /usr/local/activemq/bin $path)
set path=( /usr/local/ant/bin $path)
set path=( /usr/X11R6/bin $path)

setenv P4CONFIG .p4config
setenv P4EDITOR vim

setenv ACTIVEMQ_HOME /usr/local/activemq
setenv MAVEN_HOME /usr/local/maven
setenv ANT_HOME /usr/local/ant

#################################################################
source /home/ffaber/.aliases
#################################################################

###############################################################
# Convenient if set, but, errrrrrrr, we're not that careful sometimes..
set noclobber
set autocorrect
set blackslash_quote
set matchbeep = never
set cdpath = ~
set listjobs = long
set pushdtohome
set rmstar
set nobeep
set visiblebell

#for recursing through CVS tree w/o stumbling on CVS
#complete cd 'p/1/d:^{CVS}/'
#complete pd 'p/1/d:^{CVS}/'
#complete ddd  'n/ddd/(-q -Q -n -H -d -f -v add admin annotate ann checkout commit diff dif history log release remove status tag update watch watchers)/' 'N/release/d/' 'n/-{Q,q,n,H,d,f}/(add admin annotate ann checkout commit diff dif history log release remove status tag update watch watchers)/' 'n/release/d/' 'n/-r/$cvs_completion_tags/' 'p/*/f:^*.{o,so,a,class}/' 
#complete ls 'p/1/d:^{CVS}/'

#complete {gfsutil,fileutil,codex,wqcopy,gls,rgm} 'C@/{gfs,ls,ghost}/*/@`~${base_user}/cmd/gfs_expand $:-0`@@' 'C@*[+=]/{gfs,ls}/*/@`~/bin/gfs_expand.sh $:-0`@@'
#complete {gfsutil,fileutil,codex,wqcopy,gls,rgm} 'C@*[+=]/{gfs,ls}/*/@`~/bin/gfs_expand.sh $:-0`@@'


###############################################################

#################################################################
#         skip remaining setup if not an interactive shell
#################################################################
if ($?USER == 0 || $?prompt == 0) exit

# settings  for interactive shells
stty erase ^H
#xset b off

set history = 10000
set savehist = 10000
history -L $HOME/.history
# set ignoreeof
################################################################

alias precmd 'echo `date` `pwd` `history 1` >> ~/.shell.log'
# export PROMPT_COMMAND='if [ "$(id -u)" -ne 0 ]; then echo `date` `pwd` `history 1` >> ~/.shell.log; fi'

#if( `whoami` == 'root' ) then
#	set prompt='[root@%m %c]# ' 
#else
set prompt='[%n@%m %c]# ' 

#endif
#set prompt='%n@%m>' 
# setenv QTDIR /usr/lib/qt3

setenv HOSTNAME `hostname`
setenv LYNX_CFG /home/ffaber/lynx.cfg
