  global main
  extern printf
  section .text
main:

mov  rax, 6
mov  rcx, 2

xor rdx, rdx
div rcx

# cmp  rax, rcx
# pushf
# pop rax
# shr  rax, 6
# and rax, 1

# Post
mov  rdi, fmt
mov  rsi, rax
xor  rax, rax
call printf
ret
fmt: db `%d\n`, 0
