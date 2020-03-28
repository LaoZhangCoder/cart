import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
export function getToken() {
  return Cookies.get(TokenKey)
}

export function getRoleToken(tokenName) {
  return Cookies.get(tokenName)
}
export function setToken(token) {
  return Cookies.set(TokenKey, token)
}
export function setRoleToken(tokenName, token) {
  return Cookies.set(tokenName, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
export function removeRoleToken(tokenName) {
  return Cookies.remove(tokenName)
}
