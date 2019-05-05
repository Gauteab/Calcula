  global main
  extern printf
  section .text
main:

mov  rax, 2
mov  rcx, 3

cmp  rax, rcx
pushf
pop rax
shr  rax, 6
and rax, 1

# Post
mov  rdi, fmt
mov  rsi, rax
xor  rax, rax
call printf
ret
fmt: db `%d\n`, 0
