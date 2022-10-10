/**
 * 删除字典本地缓存
 * @param dictType
 */
export function removeDictionaryCache(dictType) {
  let key = 'rva.dict:' + dictType;
  sessionStorage.removeItem(key);
}
